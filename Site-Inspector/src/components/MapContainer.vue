<script setup>
import { onMounted, onUnmounted, defineEmits, ref } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
window._AMapSecurityConfig = {
  securityJsCode: "4588f3ef1827a8c872545cd8680d45fc",
};
const emit = defineEmits(['mapClicked'])
let map = null;
const placeSearch = ref(null);
let createMarkerInternal = (lnglat) => {
  console.error("地图尚未初始化，无法创建标记。");
}
let createPathInternal = (tasksiteinstances) => {
  console.error("地图尚未初始化，无法创建标记。");
};
let clearAllMarker = null;
onMounted(() => {
  AMapLoader.load({
    key: "59b3a0876c9217277e0767b51b30a53e", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.Scale",'AMap.PlaceSearch'], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
    .then((AMap) => {
        map = new AMap.Map("container", {
            // 设置地图容器id
            viewMode: "2D", // 是否为3D地图模式
            zoom: 15, // 初始化地图级别
            center: [117.141347, 39.060139], // 初始化地图中心点位置
        });

        const icon = new AMap.Icon({
          size: new AMap.Size(30, 32), //图标尺寸
          image: "src/assets/MapMarkerGreen.png", //Icon 的图像
          imageSize: new AMap.Size(30, 32), //根据所设置的大小拉伸或压缩图片
        });
            
        const marker = new AMap.Marker({
          position: [117.141347, 39.060139],
          offset: new AMap.Pixel(-14, -30), //偏移量
          icon : icon,
          title: "NewSitesss"
        });

        // 添加点击事件监听器
        map.on('click', (e) => {
            // 在这里处理点击事件，e.lnglat是点击位置的经纬度信息
            console.log(e.lnglat);
            emit('mapClicked', e.lnglat);
            //创建一个 Marker 实例：
            marker.setPosition(e.lnglat);
            //将创建的点标记添加到已有的地图实例：
            map.add(marker);
        });

        placeSearch.value = new AMap.PlaceSearch({
          pageSize: 5, //单页显示结果条数
          pageIndex: 1, //页码
          city: "022", //兴趣点城市
          citylimit: true, //是否强制限制在设置的城市内搜索
          map: map, //展现结果的地图实例
          panel: false, //结果列表将在此容器中进行展示。
          autoFitView: true, //是否自动调整地图视野使绘制的 Marker 点都处于视口的可见范围
        });

        const ico2n = new AMap.Icon({
          size: new AMap.Size(30, 32), //图标尺寸
          image: "src/assets/MapMarkerGreen.png", //Icon 的图像
          imageSize: new AMap.Size(30, 32), //根据所设置的大小拉伸或压缩图片
        });

        // 【生成标记相关】
        //  创建labelMarker图层
        const labelsLayer = new AMap.LabelsLayer({
          zooms: [3, 20],
          zIndex: 1000,
          collision: false, //该层内标注是否避让
          allowCollision: false, //不同标注层之间是否避让  
        });
        map.add(labelsLayer);
        createMarkerInternal = (lnglat, SiteName) =>  {
          const marker = new AMap.LabelMarker({
            position: lnglat,
            text:{
              content : SiteName
            },
            icon : {
              type: "image",
              image: "src/assets/MapMarkerRed.png", //Icon 的图像
              size: [15, 25], //图标尺寸
            },
            Opacity:0.6
          });
          console.log('锁定坐标：'+ lnglat + SiteName)
          // map.add(marker);
          labelsLayer.add(marker);
        };
        
        // 清理所有标记
        clearAllMarker = () => {
          map.clearMap()
        }

        // 【生成折线相关】
        // 配置折线路径
        
        createPathInternal = (tasksiteinstances) => {
          var path = [new AMap.LngLat(117.145942, 39.057391)]; // 天津理工大学基建处坐标
          createMarkerInternal([117.145942, 39.057391],"基建处") // 标记基建处位置
          tasksiteinstances.forEach(tasksiteinstance => {
            path.push(new AMap.LngLat(tasksiteinstance.longitude, tasksiteinstance.latitude))
          })
          //创建 Polyline 实例
          var polyline = new AMap.Polyline({
            path: path,
            strokeWeight: 2, //线条宽度
            strokeColor: "red", //线条颜色
            lineJoin: "round", //折线拐点连接处样式
          });
          map.add(polyline);
        }
    })
    .catch((e) => {
      console.log(e);
    });
});
// 创建标记：对外调用
const createMarker = (lnglat, siteName) =>{
  createMarkerInternal(lnglat, siteName)
}
// 创建路径：对外调用
const createPath = (tasksiteinstances) => {
  createPathInternal(tasksiteinstances)
}
defineExpose({
  createMarker,
  createPath
});

onUnmounted(() => {
  map?.destroy();
});

// 【地图控件相关】
const searchKeyword = ref('');
const onSearch = () => {
  if (searchKeyword.value) {
    // 调用搜索服务执行搜索
    placeSearch.value.search(searchKeyword.value);
  }
};
const clearMarkers = () => {
  clearAllMarker()
};

</script>

<template>
  <el-row :gutter = "10" style="margin: 5px 0; border: 1px solid #cdcdcd;">
    <el-col :span="24" style="margin: 5px 0">
      <el-input v-model="searchKeyword" type="text" placeholder="请输入地点" style="width: 240px"/>
      <el-button @click="onSearch" style="margin: 0 5px;">搜索</el-button>
      <el-button @click="clearMarkers" style="margin: 0;">清理标记</el-button>
    </el-col>
  </el-row>
  <el-row :gutter = "0" style="margin: 5px 0; border: 1px solid #cdcdcd;">
    <el-col :span="24" style="margin: 0">
      <div id="container" style="height:600px"></div>
    </el-col>
  </el-row>
</template>

<style scoped>
#container {
  width: 100%;
  height: 700px;
}

#my-panel {
  position: absolute;
  background-color: white;
  max-height: 90%;
  overflow-y: auto;
  top: 10px;
  right: 10px;
  width: 280px;
}
</style>