<script setup>
import { RouterLink ,RouterView } from 'vue-router';
</script>

<template>
  <el-menu
  mode="horizontal"
  class="header-menu"
  >
    <!-- <el-menu-item index="1">
      <RouterLink to = '/e' style="color:rgb(0, 0, 0)">主页</RouterLink>
    </el-menu-item> -->
    <el-menu-item index="2">
      <RouterLink to = '/SitesManage' style="color:rgb(0, 0, 0)">点位</RouterLink>
    </el-menu-item>
    <el-menu-item index="3">
      <RouterLink to = '/TaskManage' style="color:rgb(0, 0, 0)">任务管理</RouterLink>
    </el-menu-item>
    <el-menu-item index="4">
      <RouterLink to = '/UserManage' style="color:rgb(0, 0, 0)">人员管理</RouterLink>
    </el-menu-item>
  </el-menu>
  <!-- <RouterView /> -->
  <RouterView>
  </RouterView>
 
</template>

<style scoped>
.el-menu {
  background-color: #ececec;
  justify-content: center; /* 水平居中菜单 */
}
a {
  text-decoration: none;
}
.el-menu-item:hover {
  background-color: #ffffff !important; /* 使用 !important 强制覆盖 */
}
</style>