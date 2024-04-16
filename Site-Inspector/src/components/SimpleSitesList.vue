<script setup>
    import {siteManager} from '@/api/api.js';
    import {ref} from 'vue';
    
    let sites = ref([]);
    const siteIDList = ref([]);

    const getAllSites = () => {
        siteManager.getAllSitesOfOrg(0)
            .then(response => {
                sites.value = response.data;
                console.log(sites);
            });
    }
    getAllSites()

    const submit = () => {
        console.log('你好');
        console.log(siteIDList.value);
    }

    const DeleteSites = () => {
    console.log(siteIDList.value);
    siteManager.deleteSites(siteIDList.value).then((response) =>{
        toast.success(response.data.message);
        getAllSites();
    })
}
    
</script>

<template>
    <el-button @click = "getAllSites">
        刷新
    </el-button>
    <el-scrollbar height = 200>
        <el-checkbox-group v-model="siteIDList">
            <div v-for="(site, index) in sites">
                <el-checkbox  :value = "site.site_id" >
                    <p class="scrollbar-demo-item" @click="checkSite(site)">{{ index }}. {{ site.name }}</p>
            </el-checkbox>
            </div>
        </el-checkbox-group>
    </el-scrollbar>
    <el-button @click = "submit">
        提交
    </el-button>
</template>