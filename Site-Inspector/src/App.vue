<script setup>
import { RouterLink ,RouterView } from 'vue-router';
</script>

<template>
  <nav>
    <RouterLink to = '/'>Home  </RouterLink>
    <RouterLink to = '/SitesManage'>SitesManage  </RouterLink>
    <RouterLink to = '/TaskManage'>TaskManage  </RouterLink>
    <RouterLink to = '/UserManage'>UserManage</RouterLink>
  </nav>
  <!-- <RouterView /> -->
  <RouterView>
  </RouterView>
 
</template>