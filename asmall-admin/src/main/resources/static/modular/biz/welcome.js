
var welcome_vue = new Vue({
    el: '#welcome_vue',
    data: {
        statistics: { // 用户交易信息
            num1: '...',
            num2: '...',
            num3: '...',
            num4: '...',
            num5: '...',
            num6: '...',
            num7: '...',
            num8: '...'
        },
        statistics_category: [], // 柱状图数据
        myChart: null, // 柱状图
        itemNumList: [], // 出货量列表
    },
    methods: {

        /**
         * 获取出货数量
         */
        showItemNum: function(fun) {
            let this_ = this;
            window.vueUtils.ajax(null, "/item/showItemNum", function (data) {
                this_.itemNumList = data;
                if (typeof fun === "function") {
                    fun();
                }
            })
        },

        /**
         * 获取首页统计信息
         * @param fun
         */
        showStatistics: function (fun) {
            let this_ = this;
            // 删除
            vueUtils.ajax(null, '/home/welcome', function (obj) {
                // 顶部金额信息
                this_.statistics = {
                    num1: (obj.y_userNum || 0) + (obj.y_vipNum || 0),
                    num2: (obj.z_userNum || 0) + (obj.z_vipNum || 0),
                    num3: (obj.y_vipNum || 0),
                    num4: (obj.z_vipNum || 0),
                    num5: (obj.y_joinIn || 0),
                    num6: (obj.z_joinIn || 0),
                    num7: (obj.y_equity || 0),
                    num8: (obj.z_equity || 0),
                }

                // 柱状图
                this_.statistics_category = obj.statistics;
                if(typeof fun === 'function') {
                    fun();
                }
            }, function (e)  {
                console.log(e);
            });
        },

        /**
         * 初始化图形控件
         * @param fun
         */
        initGraphical: function (fun) {
            this.myChart = echarts.init(document.getElementById('echartsMain'));
            if(typeof fun === 'function') {
                fun();
            }
        },

        /**
         * 初始化图形控件数据
         * @param fun
         */
        initGraphicalData: function (fun) {
            let data1 = [['product', '订单额', '利润']];
            for(let data of this.statistics_category) {
                data1.push([data.time, data.num1, data.num2]);
            }

            // 指定图表的配置项和数据
            let option1 = {
                title : {
                    text: '最近一年订单交易额',
                    subtext: '只统计已完成的订单'
                },
                legend: {},
                tooltip: {
                    trigger: 'axis'
                },
                dataset: {
                    source: data1
                },
                xAxis: {type: 'category'},
                yAxis: {},
                series: [
                    {
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '￥{@订单额}'　　　　//这是关键，在需要的地方加上就行了
                                }
                            }
                        }
                    },
                    {
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '￥{@利润}'　　　　//这是关键，在需要的地方加上就行了
                                }
                            }
                        }
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            this.myChart.setOption(option1);

            if(typeof fun === 'function') {
                fun();
            }
        },

        /**
         * 初始化table
         */
        initTable: function () {
            console.log($('table.datatable').datatable)
            // $('table.datatable').datatable({sortable: true});
        }
    },
    mounted: function () { // 页面第一次加载时执行
        let this_ = this;
        this.$nextTick(() => {
            this_.showItemNum(function () {
                this_.initTable();
            });
            this_.showStatistics(function () {
                this_.initGraphical(function () {
                    this_.initGraphicalData();
                });
            });
        });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});

