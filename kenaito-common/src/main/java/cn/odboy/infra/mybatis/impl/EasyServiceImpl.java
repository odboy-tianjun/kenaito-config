package cn.odboy.infra.mybatis.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.odboy.infra.context.SecurityUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.mybatis.EasyMapper;
import cn.odboy.infra.mybatis.util.QUtil;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.binding.MapperMethod;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class EasyServiceImpl<M extends EasyMapper<T>, T> extends ServiceImpl<M, T> {
    public LambdaQueryWrapper<T> oh() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 快速插入
     *
     * @param resources /
     */
    public <G> int quickSave(G resources) {
        return this.getBaseMapper().insert(BeanUtil.copyProperties(resources, this.getEntityClass()));
    }

    /**
     * 快速批量插入
     *
     * @param resources /
     */
    public <G> int quickSaveBatch(List<G> resources) {
        return this.getBaseMapper().insertBatchSomeColumn(BeanUtil.copyToList(resources, this.getEntityClass()));
    }

    /**
     * 快速更新
     *
     * @param resources /
     */
    public <G> int quickModifyById(G resources) {
        return this.getBaseMapper().updateById(BeanUtil.copyProperties(resources, this.getEntityClass()));
    }

    /**
     * 快速批量更新
     *
     * @param resources /
     * @param batchSize 每批数量
     */
    public <G> boolean quickModifyBatchById(Collection<G> resources, int batchSize) {
        String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return this.executeBatch(resources, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put("et", BeanUtil.copyProperties(entity, this.getEntityClass()));
            sqlSession.update(sqlStatement, param);
        });
    }

    /**
     * 通过id查询
     *
     * @param id          /
     * @param targetClazz 目标类型
     */
    public <G> G quickGetOneById(Serializable id, Class<G> targetClazz) {
        T entity = this.getById(id);
        return entity == null ? null : BeanUtil.copyProperties(entity, targetClazz);
    }


    /**
     * 查和谁有关的一条数据
     *
     * @param wrapper 制造条件
     */
    public T quickGetOneWho(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn) {
        if (orderColumn == null) {
            throw new BadRequestException("参数orderColumn(排序的列)必填");
        }
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        wrapper.last("limit 1");
        wrapper.orderByDesc(orderColumn);
        List<T> ts = baseMapper.selectList(wrapper);
        if (ts.isEmpty()) {
            return null;
        }
        return ts.stream().findFirst().get();
    }

    /**
     * 查和谁有关的一条数据
     *
     * @param wrapper 制造条件
     */
    public <G> G quickGetOneWho(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz) {
        if (orderColumn == null) {
            throw new BadRequestException("参数orderColumn(排序的列)必填");
        }
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        wrapper.last("limit 1");
        wrapper.orderByDesc(orderColumn);
        List<T> ts = baseMapper.selectList(wrapper);
        if (ts.isEmpty()) {
            return null;
        }
        return BeanUtil.copyProperties(ts.stream().findFirst().get(), clazz);
    }

    /**
     * 查一条数据
     *
     * @param wrapper 制造条件
     */
    public T quickGetOne(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn) {
        if (orderColumn == null) {
            throw new BadRequestException("参数orderColumn(排序的列)必填");
        }
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.last("limit 1");
        wrapper.orderByDesc(orderColumn);
        List<T> ts = baseMapper.selectList(wrapper);
        if (ts.isEmpty()) {
            return null;
        }
        return ts.stream().findFirst().get();
    }

    public <G> G quickGetOne(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz) {
        if (orderColumn == null) {
            throw new BadRequestException("参数orderColumn(排序的列)必填");
        }
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.last("limit 1");
        wrapper.orderByDesc(orderColumn);
        List<T> ts = baseMapper.selectList(wrapper);
        if (ts.isEmpty()) {
            return null;
        }
        return BeanUtil.copyProperties(ts.stream().findFirst().get(), clazz);
    }

    /**
     * 通过id集合查询
     *
     * @param ids         /
     * @param targetClazz 目标类型
     */
    public <G> List<G> quickListByIds(List<Serializable> ids, Class<G> targetClazz) {
        List<T> entitys = this.listByIds(ids);
        return entitys == null || entitys.isEmpty() ? CollUtil.newArrayList() : BeanUtil.copyToList(entitys, targetClazz);
    }

    /**
     * 查和谁有关的数据
     *
     * @param wrapper 制造条件
     */
    public <G> List<G> quickListWho(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), targetClazz);
    }

    /**
     * 查和谁有关的数据
     *
     * @param criteria 制造条件
     */
    public <G, Q> List<G> quickListWho(Q criteria, Class<G> targetClazz) {
        QueryWrapper<T> wrapper = QUtil.build(criteria);
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), targetClazz);
    }

    /**
     * 查有关的数据
     *
     * @param criteria 动态条件
     */
    public <Q> List<T> quickList(Q criteria) {
        QueryWrapper<T> predicate = QUtil.build(criteria);
        return baseMapper.selectList(predicate);
    }

    /**
     * 查有关的数据
     *
     * @param wrapper 制造条件
     */
    public List<T> quickList(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 查有关的数据并转成目标类型的数据
     *
     * @param criteria    动态条件
     * @param targetClazz 目标类型
     */
    public <G, Q> List<G> quickList(Q criteria, Class<G> targetClazz) {
        QueryWrapper<T> predicate = QUtil.build(criteria);
        List<T> data = baseMapper.selectList(predicate);
        return BeanUtil.copyToList(data, targetClazz);
    }

    /**
     * 查有关的数据并转换为目标类型
     *
     * @param wrapper     制造条件
     * @param targetClazz 目标类型
     */
    public <G> List<G> quickList(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), targetClazz);
    }

    /**
     * 查有关的数据
     *
     * @param wrapper 制造条件
     */
    public List<T> quickListWho(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return baseMapper.selectList(wrapper);
    }

    /**
     * 查有关的数据的总数
     *
     * @param wrapper 制造条件
     */
    public long quickCountWho(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return baseMapper.selectCount(wrapper);
    }

    /**
     * 查有关的分页数据
     *
     * @param wrapper  制造条件
     * @param pageable 分页参数
     */
    public PageResult<T> quickPage(LambdaQueryWrapper<T> wrapper, IPage<T> pageable) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(originPageData.getRecords(), originPageData.getTotal());
    }

    /**
     * 查有关的分页数据
     *
     * @param wrapper     制造条件
     * @param pageable    分页参数
     * @param targetClazz 目标类型
     */
    public <G> PageResult<G> quickPage(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(BeanUtil.copyToList(originPageData.getRecords(), targetClazz), originPageData.getTotal());
    }

    public <G, Q> PageResult<G> quickPage(Q criteria, IPage<T> pageable, Class<G> targetClazz) {
        QueryWrapper<T> wrapper = QUtil.build(criteria);
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(BeanUtil.copyToList(originPageData.getRecords(), targetClazz), originPageData.getTotal());
    }

    public <Q> PageResult<T> quickPage(Q criteria, IPage<T> pageable) {
        QueryWrapper<T> wrapper = QUtil.build(criteria);
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(originPageData.getRecords(), originPageData.getTotal());
    }

    /**
     * 条件更新
     *
     * @param wrapper 制造条件
     * @param entity  更新内容
     */
    public int quickUpdate(LambdaQueryWrapper<T> wrapper, T entity) {
        if (wrapper == null) {
            throw new BadRequestException("参数wrapper必填");
        }
        return baseMapper.update(entity, wrapper);
    }

    /**
     * 指定条件更新谁的数据
     *
     * @param wrapper 制造条件
     * @param entity  更新内容
     */
    public int quickUpdateWho(LambdaQueryWrapper<T> wrapper, T entity) {
        if (wrapper == null) {
            throw new BadRequestException("参数wrapper必填");
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return baseMapper.update(entity, wrapper);
    }

    /**
     * 条件删除
     *
     * @param wrapper 制造条件
     */
    public int quickDelete(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            throw new BadRequestException("参数wrapper必填");
        }
        return baseMapper.delete(wrapper);
    }

    /**
     * 指定条件删除谁的数据
     *
     * @param wrapper 制造条件
     */
    public int quickDeleteWho(LambdaQueryWrapper<T> wrapper) {
        if (wrapper == null) {
            throw new BadRequestException("参数wrapper");
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        return baseMapper.delete(wrapper);
    }

    /**
     * 查和谁有关的分页数据
     *
     * @param wrapper     制造条件
     * @param pageable    分页参数
     * @param targetClazz 目标类型
     */
    public <G> PageResult<G> quickPageWho(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(BeanUtil.copyToList(originPageData.getRecords(), targetClazz), originPageData.getTotal());
    }

    /**
     * 查和谁有关的分页数据
     *
     * @param criteria    制造条件
     * @param pageable    分页参数
     * @param targetClazz 目标类型
     */
    public <G, Q> PageResult<G> quickPageWho(Q criteria, IPage<T> pageable, Class<G> targetClazz) {
        QueryWrapper<T> wrapper = QUtil.build(criteria);
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(BeanUtil.copyToList(originPageData.getRecords(), targetClazz), originPageData.getTotal());
    }

    /**
     * 查和谁有关的分页数据
     *
     * @param wrapper  制造条件
     * @param pageable 分页参数
     */
    public PageResult<T> quickPageWho(LambdaQueryWrapper<T> wrapper, IPage<T> pageable) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.apply("create_by = {0}", SecurityUtil.getCurrentUsername());
        IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
        return new PageResult<>(originPageData.getRecords(), originPageData.getTotal());
    }
}
