<template>
  <div class="app-container">
    <!--查询条件-->
    <span>
      <el-input
        v-model="myQuery.appName"
        class="filter-item"
        clearable
        size="small"
        placeholder="请输入应用名称"
        style="width: 200px;"
        @keyup.enter.native="toQuery"
      />
      <el-button
        class="filter-item"
        size="mini"
        type="success"
        icon="el-icon-search"
        style="margin-left: 5px"
        @click="toQuery"
      >搜索</el-button>
      <el-button
        v-permission="permission.add"
        class="filter-item"
        size="mini"
        type="primary"
        icon="el-icon-plus"
        @click="toAdd"
      >新增应用</el-button>
    </span>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card>
          <!--表格渲染-->
          <el-table
            ref="myTable"
            :data="myTable.data"
            row-key="id"
            highlight-current-row
            @row-click="handleCurrentRowChange"
          >
            <el-table-column label="应用名称" prop="appName" width="200" />
            <el-table-column label="应用描述" prop="description" />
            <el-table-column v-if="checkPer(['admin','app:edit'])" label="操作" width="100" align="center" fixed="right">
              <template slot-scope="scope">
                <span>
                  <el-button type="text" size="mini" @click.stop="onRemoveBtnClick(scope.row)">删除</el-button>
                </span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page="myTable.page"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="myTable.pageSize"
            layout="total, sizes, prev, pager, next"
            :total="myTable.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
        <el-card style="margin-top: 10px">
          <el-table
            :data="clientListData"
          >
            <el-table-column label="IP地址" prop="ip" />
            <el-table-column label="是否在线" prop="isActive">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.isActive" type="success">online</el-tag>
                <el-tag v-else type="error">offline</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <el-descriptions :column="2" border>
            <template slot="extra">
              <el-button
                v-permission="permission.del"
                size="mini"
                type="danger"
                icon="el-icon-plus"
                @click="onDeleteEnv"
              >删除环境</el-button>
              <el-button
                v-permission="permission.add"
                size="mini"
                type="primary"
                icon="el-icon-plus"
                @click="onAddEnv"
              >新增环境</el-button>
              <el-button
                v-permission="permission.add"
                size="mini"
                type="primary"
                icon="el-icon-plus"
                @click="uploadConfigFile"
              >上传配置</el-button>
            </template>
            <el-descriptions-item>
              <template slot="label">
                当前应用
              </template>
              <font v-if="currentRow.appName && currentRow.appName !== ''">{{ currentRow.appName }}</font>
              <font v-else color="red">未选中应用</font>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                环境
              </template>
              <el-select
                v-model="currentEnvCode"
                clearable
                placeholder="请选择所属环境"
                style="width: 100%"
                @visible-change="handleAppEnvVisibleChange"
                @change="handleAppEnvChange"
              >
                <el-option
                  v-for="item in envCodeDataSource"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                操作
              </template>
              <el-button type="primary">发布</el-button>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
        <el-card style="margin-top: 20px">
          <el-table :data="envConfigFileDataSource" row-key="id">
            <el-table-column label="文件名称" prop="fileName" />
            <el-table-column label="文件类型" prop="fileType" />
            <el-table-column label="版本号" prop="version" />
            <el-table-column label="创建人" prop="createBy" />
            <el-table-column label="创建时间" prop="createTime" />
            <el-table-column v-if="checkPer(['admin','app:edit'])" label="操作" width="120" align="center" fixed="right">
              <template slot-scope="scope">
                <span>
                  <el-button type="text" size="mini" @click.stop="onEditBtnClick(scope.row)">编辑</el-button>
                  <el-button type="text" size="mini" @click.stop="onRemoveConfigFileBtnClick(scope.row)">删除</el-button>
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :visible.sync="form.addVisible"
      title="新增应用"
      width="700px"
    >
      <el-form ref="addForm" inline :model="form.addValues" :rules="form.rules" size="small" label-width="120px">
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="form.addValues.appName" placeholder="请填写应用名称" style="width: 450px" />
        </el-form-item>
        <el-form-item label="应用描述" prop="description">
          <el-input v-model="form.addValues.description" placeholder="请填写应用描述" style="width: 450px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onCancelBtnClick('addForm')">取消</el-button>
        <el-button :loading="form.addLoading" type="primary" @click="onAddSubmitBtnClick('addForm')">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :visible.sync="editForm.visible"
      title="编辑配置文件"
      width="900px"
    >
      <el-form ref="editForm" inline :model="editForm.values" size="small" label-width="120px">
        <el-form-item label="" prop="fileContent">
          <PropertiesEdit ref="fileContentEdit" v-model="editForm.values.fileContent" height="500px" :read-only="false" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onEditFileContentCancelBtnClick('editForm')">取消</el-button>
        <el-button :loading="editForm.loading" type="primary" @click="onEditFileContentSubmitBtnClick('editForm')">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :visible.sync="uploadForm.visible"
      title="上传配置"
      width="400px"
    >
      <el-form ref="uploadForm" inline :model="uploadForm.values" :rules="uploadForm.rules" size="small" label-width="120px">
        <el-form-item label="" prop="file">
          <el-upload
            ref="uploadClient"
            v-model="uploadForm.values.file"
            drag
            :headers="uploadForm.headers"
            :action="baseApi +'/api/config/file/upload'"
            :auto-upload="false"
            accept=".properties,.yml,.yaml"
            multiple
            name="file"
            :data="{
              appId: currentRow.id,
              envCode: currentEnvCode
            }"
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传properties/yaml文件，且不超过1mb</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onUploadFormCancelBtnClick('uploadForm')">取消</el-button>
        <el-button :loading="uploadForm.loading" type="primary" @click="onUploadFormSubmitBtnClick('uploadForm')">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as configAppService from '@/api/config/configApp'
import * as configAppEnvService from '@/api/config/configAppEnv'
import * as configFileService from '@/api/config/configFile'
import { MessageBoxUtil, MessageUtil } from '@/utils/myElement'
import PropertiesEdit from '@/components/PropertiesEdit/index.vue'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'

export default {
  name: 'ConfigCenter',
  components: { PropertiesEdit },
  data() {
    return {
      permission: {
        add: ['admin', 'app:add'],
        edit: ['admin', 'app:edit'],
        del: ['admin', 'app:del']
      },
      myQuery: {
        appName: null
      },
      myTable: {
        page: 1,
        pageSize: 10,
        total: 0,
        data: []
      },
      form: {
        addLoading: false,
        addVisible: false,
        addValues: {
          appName: '',
          description: ''
        },
        rules: {
          appName: [
            { required: true, message: '请输入应用名称', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入应用描述', trigger: 'blur' }
          ]
        }
      },
      currentRow: {},
      envCodeDataSource: [],
      currentEnvCode: '',
      envConfigFileDataSource: [],
      // 编辑文件内容
      editForm: {
        loading: false,
        visible: false,
        currentId: null,
        values: {
          fileContent: ''
        },
        rules: {
          fileContent: [
            { required: true, message: '请输入文件内容', trigger: 'blur' }
          ]
        }
      },
      uploadForm: {
        loading: false,
        visible: false,
        headers: { 'Authorization': getToken() },
        values: {
          file: null
        }
      },
      clientListData: []
    }
  },
  computed: {
    ...mapGetters([
      'baseApi'
    ])
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {
      this.loadPage(this.myTable.page, this.myTable.pageSize)
    },
    loadPage(page, pageSize) {
      const _this = this
      const data = {
        page: page,
        pageSize: pageSize,
        body: {
          ..._this.myQuery
        }
      }
      configAppService.queryPage(data).then(res => {
        _this.myTable.data = res.records || []
        _this.myTable.total = res.total
      })
    },
    handleSizeChange(pageSize) {
      this.myTable.pageSize = pageSize
      this.loadPage(1, pageSize)
    },
    handleCurrentChange(page) {
      this.myTable.page = page
      this.loadPage(page, this.myTable.pageSize)
    },
    toQuery() {
      this.loadPage(this.myTable.page, this.myTable.pageSize)
    },
    // ok
    toAdd() {
      this.form.addValues.appName = ''
      this.form.addValues.description = ''
      this.form.addVisible = true
    },
    // ok
    onCancelBtnClick(formName) {
      this.$refs[formName].resetFields()
      this.form.addVisible = false
    },
    // ok
    onAddSubmitBtnClick(formName) {
      const _this = this
      _this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.form.addLoading = true
          configAppService.create({
            ..._this.form.addValues
          }).then(res => {
            MessageUtil.success(_this, '操作成功')
            _this.form.addLoading = false
            _this.form.addVisible = false
            _this.initData()
          }).catch(e => {
            _this.form.addLoading = false
          })
        } else {
          return false
        }
      })
    },
    // ok
    handleCurrentRowChange(row) {
      const _this = this
      _this.currentRow = row
      configAppEnvService.queryList({ appId: row.id }).then(res => {
        _this.envCodeDataSource = res || []
      })
    },
    // ok
    onRemoveBtnClick(row) {
      const _this = this
      MessageBoxUtil.deleteMessageConfirm(_this, '是否删除当前应用?', () => {
        configAppService.remove(row).then(res => {
          MessageUtil.success(_this, '操作成功')
          _this.toQuery()
        }).catch(e => {
        })
      }, null)
    },
    // ok
    uploadConfigFile() {
      const _this = this
      const currentEnvCode = _this.currentEnvCode
      if (currentEnvCode === undefined || currentEnvCode === null || currentEnvCode === '') {
        MessageUtil.error(_this, '请选择环境后，再试')
        return
      }
      _this.uploadForm.values.file = null
      _this.uploadForm.visible = true
    },
    // ok
    onEditBtnClick(row) {
      const _this = this
      configFileService.getContentById({ id: row.id }).then(res => {
        _this.editForm.currentId = row.id
        _this.editForm.values.fileContent = res || ''
        _this.editForm.visible = true
      })
    },
    // dev
    onRemoveConfigFileBtnClick(row) {
      const _this = this
      MessageBoxUtil.deleteMessageConfirm(_this, '是否删除当前配置文件?', () => {
        configFileService.remove(row).then(res => {
          MessageUtil.success(_this, '操作成功')
          const currentRow = _this.currentRow
          configFileService.queryList({
            appId: currentRow.id,
            envCode: _this.currentEnvCode
          }).then(res => {
            _this.envConfigFileDataSource = res || []
          })
        }).catch(e => {
        })
      }, null)
    },
    // ok
    handleAppEnvVisibleChange(visible) {
      if (visible && this.envCodeDataSource.length === 0) {
        MessageUtil.error(this, '请选中应用或添加环境')
      }
    },
    // ok
    handleAppEnvChange(envCode) {
      const _this = this
      _this.currentEnvCode = envCode
      const currentRow = _this.currentRow
      if (envCode) {
        // query file list
        configFileService.queryList({
          appId: currentRow.id,
          envCode: envCode
        }).then(res => {
          _this.envConfigFileDataSource = res || []
        })
        // query client list
        configAppService.queryClientList({
          envCode: envCode,
          appName: currentRow.appName
        }).then(res => {
          _this.clientListData = res || []
        })
      }
    },
    // ok
    onDeleteEnv() {
      const _this = this
      if (_this.currentEnvCode === undefined || _this.currentEnvCode === null || _this.currentEnvCode === '') {
        MessageUtil.error(_this, '请选择应用和环境后，再试')
        return
      }
      MessageBoxUtil.deleteMessageConfirm(_this, '是否删除当前环境?', () => {
        configAppEnvService.remove({
          appId: _this.currentRow.id,
          envCode: _this.currentEnvCode
        }).then(res => {
          MessageUtil.success(_this, '操作成功')
          configAppEnvService.queryList({ appId: _this.currentRow.id }).then(res => {
            _this.envCodeDataSource = res || []
          })
          _this.envConfigFileDataSource = []
          _this.currentEnvCode = ''
        }).catch(e => {
        })
      }, null)
    },
    // ok
    onAddEnv() {
      const _this = this
      const currentRow = _this.currentRow
      _this.$prompt('请输入环境编码', '', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-z]+$/,
        inputErrorMessage: '请输入正确的环境编码，环境编码只能由小写字母组成'
      }).then(({ value }) => {
        _this.doAddEnv(currentRow, value)
      })
    },
    // ok
    doAddEnv(currentRow, envCode) {
      const _this = this
      configAppEnvService.create({
        appId: currentRow.id,
        envCode: envCode
      }).then(res => {
        MessageUtil.success(_this, '操作成功')
        configAppEnvService.queryList({ appId: currentRow.id }).then(res => {
          _this.envCodeDataSource = res || []
        })
      })
    },
    // ok
    onEditFileContentCancelBtnClick(formName) {
      this.$refs[formName].resetFields()
      this.editForm.visible = false
    },
    // ok
    onEditFileContentSubmitBtnClick(formName) {
      const _this = this
      _this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.editForm.loading = true
          configFileService.modifyFileContent({
            id: _this.editForm.currentId,
            fileContent: _this.$refs.fileContentEdit.getValue()
          }).then(res => {
            MessageUtil.success(_this, '操作成功')
            _this.editForm.loading = false
            _this.editForm.visible = false
            _this.initData()
            configFileService.queryList({
              appId: _this.currentRow.id,
              envCode: _this.currentEnvCode
            }).then(res => {
              _this.envConfigFileDataSource = res || []
            })
          }).catch(e => {
            _this.editForm.loading = false
          })
        } else {
          return false
        }
      })
    },
    onUploadFormCancelBtnClick(formName) {
      this.$refs[formName].resetFields()
      this.uploadForm.visible = false
      this.uploadForm.loading = false
    },
    onUploadFormSubmitBtnClick(formName) {
      const _this = this
      _this.$refs[formName].validate((valid) => {
        if (valid) {
          _this.uploadForm.loading = true
          _this.$refs.uploadClient.submit()
          setTimeout(() => {
            configFileService.queryList({
              appId: _this.currentRow.id,
              envCode: _this.currentEnvCode
            }).then(res => {
              _this.envConfigFileDataSource = res || []
            })
          }, 2000)
          MessageUtil.success(_this, '上传成功')
          _this.uploadForm.loading = false
          _this.uploadForm.values.file = null
          _this.uploadForm.visible = false
        } else {
          return false
        }
      })
    }
  }
}
</script>

