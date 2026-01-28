// amp.js
import AMapLoader from "@amap/amap-jsapi-loader";
import { usePosStore } from "../store/position";
// 统一加载器，避免多次重复加载
const loadAMap = () => {
    window._AMapSecurityConfig = {
        securityJsCode: "f5e1079bd4de325017523e7ae5440b64", // 安全密钥
    };
    return AMapLoader.load({
        key: "b2a4a5c89600897e006d21962f349a27",
        version: "2.0",
        plugins: ["AMap.Scale", "AMap.Geocoder", "AMap.Geolocation"],
    });
};

const GEO_CACHE_KEY = "geo";
export function AMap() {
    loadAMap().then((AMap) => {
        AMap.plugin("AMap.Geolocation", function () {
            const geolocation = new AMap.Geolocation({
                // 是否使用高精度定位，默认：true
                enableHighAccuracy: true,
                // 设置定位超时时间，默认：无穷大
                timeout: 10000,
                // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
                buttonOffset: new AMap.Pixel(10, 20),
                //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                zoomToAccuracy: true,
                //  定位按钮的排放位置,  RB表示右下
                buttonPosition: "RB",
            });
            geolocation.getCurrentPosition(function (status, result) {
                if (status == "complete") {
                    onComplete(result);
                }
                if (status == "error") {
                    onError(result);
                }
            });
            function onComplete(data) {
                console.log("定位成功：", data);
                // 缓存 lng/lat 备用
                if (data?.position?.lng && data?.position?.lat) {
                    getAddressByLngLat(data.position.lng, data.position.lat);
                }
            }
            function onError(data) {
                console.log(data);
                // data是具体的定位信息
            }
        });
    });
}

// 逆地理编码：经纬度 -> 省市区县及详细地址
// lng/lat 可选，不传则读取最近缓存
export function getAddressByLngLat(lng, lat) {
    return new Promise((resolve, reject) => {
        if (!lng || !lat) {
            reject('无有效经纬度||经纬度缺失');
            return;
        }
        loadAMap()
            .then((AMap) => {
                AMap.plugin("AMap.Geocoder", function () {
                    const geocoder = new AMap.Geocoder({
                        extensions: "all",
                    });
                    geocoder.getAddress([lng, lat], function (status, result) {
                        if (status === "complete" && result?.regeocode) {
                            const comp = result.regeocode.addressComponent;

                            const posStore = usePosStore();
                            posStore.setPosition(comp.district || comp.city || comp.province);
                            resolve({
                                formatted: result.regeocode.formattedAddress,
                                province: comp.province,
                                city: comp.city || comp.province,
                                district: comp.district, // 区/县
                                township: comp.township,
                                adcode: comp.adcode,
                            });
                        } else {
                            reject(result);
                        }
                    });
                });
            })
            .catch(reject);
    });
}
