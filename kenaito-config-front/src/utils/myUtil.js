import store from '@/store'

const PermissionUtil = {
  /**
   * 权限校验
   * @param value
   * @returns {boolean|*}
   */
  validate: function(value) {
    if (value && value instanceof Array && value.length > 0) {
      // 拥有的权限
      const roles = store.getters && store.getters.roles
      // 需要的权限
      const permissionRoles = value
      return roles.some(role => {
        return permissionRoles.includes(role)
      })
    } else {
      console.error(`need roles! Like v-permission="['admin','editor']"`)
      return false
    }
  }
}

const StringUtil = {
  /**
   * 字符串转数字
   * @param value
   * @returns {Number|*}
   */
  toNumber: function(value) {
    if (value) {
      return Number(value)
    }
    return 0
  },
  /**
   * 保留几位小数
   * @param num Number
   * @param accuracy 精度
   * @returns {string|number}
   */
  retailNum: function(num, accuracy) {
    return num instanceof Number ? num.toFixed(accuracy) : 0
  }
}

const ObjectUtil = {
  /**
   * 浅拷贝
   * @param source 源对象
   * @param target 目标对象
   * @returns {*}
   */
  copy: function(source, target) {
    return Object.assign(target, source)
  }
}

const LabelUtil = {
  /**
   * 根据value获取label
   * @param options /
   * @param value /
   * @returns {*}
   */
  getLabel: function(options, value) {
    if (options instanceof Array) {
      const find = options.find(f => f.value === value)
      if (find) {
        return find.label
      }
      return ''
    }
  }
}

export {
  PermissionUtil,
  StringUtil,
  ObjectUtil,
  LabelUtil
}
