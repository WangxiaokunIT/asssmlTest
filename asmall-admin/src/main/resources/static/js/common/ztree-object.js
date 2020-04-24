/**
 * ztree插件的封装
 */
(function() {

	var $ZTree = function(id, url,onClick) {
		this.id = id;
		this.url = url;
		this.onClick = onClick;
		this.settings = null;
		this.ondblclick=null;
        this.onAsyncSuccess=null;
        this.beforeDrag=null;
        this.beforeDrop=null;
        this.onDrop=null;
		this.onRightClick=null;
	};

	$ZTree.prototype = {
		/**
		 * 初始化ztree的设置
		 */
		initSetting : function() {
			var settings = {
				view : {
					dblClickExpand : true,
					selectedMulti : false
				},
				data : {simpleData : {enable : true,pIdKey:"parentId"}},
				callback : {
					onClick : this.onClick,
					onDblClick:this.ondblclick

				}
			};
			return settings;
		},
        /**
         * 初始化ztree的异步设置
         */
        initSyncSetting : function() {
            var settings = {
                view : {
                    dblClickExpand : true,
                    selectedMulti : false,
                    fontCss: this.getFont,
                    nameIsHTML: true
                },
                data : {simpleData : {enable : true,pIdKey:"parentId"}},
                callback : {
                    onClick : this.onClick,
                    onDblClick:this.ondblclick,
                    onAsyncSuccess:this.onAsyncSuccess,
                    onRightClick: this.onRightClick,
                    beforeDrag: this.beforeDrag,
                    beforeDrop: this.beforeDrop,
                    onDrop:this.onDrop,
                },
				async:{
                    enable: true,
                    url: this.url,
                    autoParam: ["id=parentId","type"],
                    dataFilter: this.dataFilter
                },edit: {
                    enable: true,
					showRenameBtn: false,
                    showRemoveBtn:false,
                	drag :{
						isCopy:false,
						isMove:true,
        				prev: false,
        				next: false,
        				inner: true
					}
                }
            };
            return settings;
        },

		/**
		 * 手动设置ztree的设置
		 */
		setSettings : function(val) {
			this.settings = val;
		},

		/**
		 * 初始化ztree
		 */
		init : function() {
			var zNodeSeting = null;
			if(this.settings != null){
				zNodeSeting = this.settings;
			}else{
				zNodeSeting = this.initSetting();
			}
			var zNodes = this.loadNodes();
			$.fn.zTree.init($("#" + this.id), zNodeSeting, zNodes);
		},

        /**
         * 初始化异步加载ztree
         */
        initSync : function() {
            var zNodeSeting = null;
            if(this.settings != null){
                zNodeSeting = this.settings;
            }else{
                zNodeSeting = this.initSyncSetting();
            }
            $.fn.zTree.init($("#" + this.id), zNodeSeting);
        },

        /**
		 * 绑定onclick事件
		 */
		bindOnClick : function(func) {
			this.onClick = func;
		},
		/**
		 * 绑定双击事件
		 */
		bindOnDblClick : function(func) {
			this.ondblclick=func;
		},
        /**
         * 绑定异步回调事件
         */
        bindAsyncSuccess : function(func) {
            this.onAsyncSuccess = func;
        },
		/**
		 * 加载节点
		 */
		loadNodes : function() {
			var zNodes = null;
			var ajax = new $ax(Feng.ctxPath + this.url, function(data) {
				zNodes = data;
			}, function(data) {
				Feng.error("加载ztree信息失败!");
			});
			ajax.start();
			return zNodes;
		},
        /**
		 * 加载节点样式
         * @param treeId
         * @param node
         * @returns {*}
         */
		getFont:function(treeId, node) {
        	return node.font ? node.font : {};
    	},
		/**
		 * 获取选中的值
		 */
		getSelectedVal : function(){
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var nodes = zTree.getSelectedNodes();
			return nodes[0].name;
		},/**
         * 设置选中的值
         */
        setSelectedVal : function(value){
        	if(value){
                var zTree = $.fn.zTree.getZTreeObj(this.id);
                //zTree.expandAll(true);
                var node = zTree.getNodeByParam("id", value, null);
                if(node){
                    zTree.selectNode(node);
                    $("#"+node.tId+"_a").click(); // 点击节点
				}
			}
        },
        /**
		 * 用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
         * @param treeId
         * @param treeNodes
         * @returns {boolean}
         */
        bindBeforeDrag : function(func) {
            this.beforeDrag = func;
		},
        /**
		 * 用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作
         * @param func
         */
        bindBeforeDrop : function(func){
            this.beforeDrop = func;
		},
        /**
		 * 用于捕获节点拖拽操作结束的事件回调函数
         * @param func
         */
		bingOnDrop : function(func){
            this.onDrop = func;
		},
        /**
		 * 右击事件
         * @param func
         */
		bindOnRightClick : function(func){
            this.onRightClick = func;
		},
        refresh : function(){
            var zTree = $.fn.zTree.getZTreeObj(this.id);
            zTree.refresh();
        }
	};

	window.$ZTree = $ZTree;

}());