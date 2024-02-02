var jqGrid_formater = [];
var dlgPopup;

function NewCssCal(pCtrl, pFormat, pScroller, pShowTime, pTimeMode, pShowSeconds) {
	console.log('pShowTime=0:' + pShowTime);
	var _timeFormat = "HH:mm:ss";
	if (!pShowSeconds) {
		_timeFormat = "HH:mm";
	}
	if (pShowTime) {
		console.log('NewCssCal=1');
		$('#' + pCtrl).datetimepicker({
			timeInput: pShowTime,
			showTime: pShowTime,
			dateFormat: "dd/mm/yy",
			timeFormat: _timeFormat,
			showHour: false,
			showMinute: false,
			showSecond: false
		});
		$('#' + pCtrl).datepicker("show");
	}
	else {
		console.log('NewCssCal=2 pScroller=' + pScroller);
		if (pScroller == 'month') {
			console.log('NewCssCal=3');
			$('#' + pCtrl).MonthPicker({
				//MinMonth: 5,
				//MaxMonth: 5, 
				//MonthFormat: 'm, yy',
				Button: false
			});
			$('#' + pCtrl).MonthPicker('Open');
		}
		else {
			console.log('NewCssCal=4');
			$('#' + pCtrl).datepicker({
				dateFormat: "dd/mm/yy"
			});
			$('#' + pCtrl).datepicker("show");
		}
	}

	//$('.ui-datepicker-div').css('zIndex', 10099);
}


Element.prototype.getElementWidth = function () {
	if (typeof this.clip !== "undefined") {
		return this.clip.width;
	} else {
		if (this.style.pixelWidth) {
			return this.style.pixelWidth;
		} else {
			return this.offsetWidth;
		}
	}
};
/**/

$.sleep = function (milliseconds) {
	var start = new Date().getTime();

	var timer = true;
	while (timer) {
		if ((new Date().getTime() - start) > milliseconds) {
			timer = false;
		}
	}
}
$.waitUntil = function (isready, success, error, count, interval) {
	console.log('waitUntil count=' + count);
	if (count === undefined) {
		count = 300;
	}
	if (interval === undefined) {
		interval = 20;
	}
	if (isready()) {
		success();
		return;
	}
	// The call back isn't ready. We need to wait for it
	$.sleep(interval);
	if (!count) {
		// We have run out of retries
		if (error !== undefined) {
			error();
		}
	} else {
		// Try again
		$.waitUntil(isready, success, error, count - 1, interval);
	}

	/*
	setTimeout(function(){
		if (!count) {
			// We have run out of retries
			if (error !== undefined) {
				error();
			}
		} else {
			// Try again
			$.waitUntil(isready, success, error, count -1, interval);
		}
	}, interval);
	*/
}
//--------- ButtonUtil --------------------------------------------------------------------------------------
$.fn.bindOnce = function (event, callback, _waitTime) {
	console.log('bind ' + event);
	var element = $(this[0]),
		defer = element.data("bind_once_defer_" + event);

	if (!defer) {
		defer = $.Deferred();
		function deferCallback() {
			console.log('unbind ' + event);
			element.unbind(event, deferCallback);
			defer.resolveWith(this, arguments);
		}
		element.bind(event, deferCallback)
		element.data("bind_once_defer_" + event, defer);
	}
	defer.done(callback).then(function () {
		//console.log('callback done!');
		element.data("bind_once_defer_" + event, false);
		//var milliseconds=10000;
		if (!_waitTime) _waitTime = 10000;
		console.log('delay ' + event + ' _waitTime=' + _waitTime);
		setTimeout(function () {
			$(element).bindOnce(event, callback, _waitTime);
		}, _waitTime);
	});

	//return defer.done( callback ).promise();
};

var ButtonUtil = {
	bindClick: function (btnId, _func) {
		$('#' + btnId).off();
		$('#' + btnId).on("click", function (evt) {
			var $btn = $(this);
			if ($btn.data('flag') === true) {
				// Previously submitted - don't submit again
				e.preventDefault();

			} else {
				// Mark it so that the next submit can be ignored
				$btn.data('flag', true);
				$.done(_func(evt)).then();
			}
		});
	}
}

//--------- EventUtil --------------------------------------------------------------------------------------
var EventUtil = {
	eventPool: {},
	varPool: {},
	getVar: function (varName) {
		return this.varPool[varName];
	},
	setVar: function (varName, varObj) {
		this.varPool[varName] = varObj;
	},

	getEvent: function (evName) {
		console.log('getEvent ' + evName);
		return this.eventPool[evName];
	},
	setEvent: function (evName, evFunc) {
		console.log('setEvent ' + evName + (typeof evFunc));
		this.eventPool[evName] = evFunc;
	},
	raiseEvent: function (evName, evObj) {
		console.log('setEvent ' + evName + (typeof evFunc));
		var evFunc = this.eventPool[evName];
		if (typeof evFunc === 'function') {
			evFunc(evObj);
		}
		else {
			console.log('evFunc not a function');
		}
	}
};
//--------- DlgUtil --------------------------------------------------------------------------------------
var DlgUtil = {
	dlg: [],
	buildPopupGrid: function (dlgId, gridId, _title, _width, _height) {
		//popup 1 grid
		$('#' + dlgId).html('<table id="' + gridId + '"></table><div id="pager_' + gridId + '"></div>');
		dlgPopup = new jBox('Modal', {
			title: _title,
			closeOnClick: false,
			closeButton: 'title',
			overlay: true,
			content: $('#' + dlgId),
			draggable: 'title',
			width: _width,
			height: _height,
			onClose: function () {
				EventUtil.raiseEvent(dlgId + '_onClose', dlgId);
			}
		});
		return dlgPopup;
	},
	showMsg: function (msgInfo, _func, _delay, _css) {

		//DlgUtil.showMsg("Nội dung thông báo","Thông báo",0);
		//alert(msgInfo);
		alertify.set({
			labels: {
				ok: "Đồng ý",
				cancel: "Hủy bỏ"
			},
			delay: 5000,
			buttonReverse: false,
			buttonFocus: "ok"
		});
		console.log('showMsg _delay=' + _delay);
		alertify.alert(msgInfo, _func, _css, _delay);
		/*
		$.waitUntil(function(){
			console.log('waitUntil.waitFlag='+waitFlag);
			return !waitFlag;
		}, function(){}, function(){},10,1000);
		*/

	},

	showConfirm: function (msgInfo, _func, _delay) {
		//DlgUtil.showMsg("Nội dung thông báo","Thông báo",0);
		//alert(msgInfo);
		alertify.set({
			labels: {
				ok: "Đồng ý",
				cancel: "Hủy bỏ"
			},
			delay: 5000,
			buttonReverse: false,
			buttonFocus: "ok"
		});
		alertify.confirm(msgInfo, _func, "", _delay);
	},


	buildPopupUrl: function (dlgName, dlgId, _url, _var, _title, _width, _height, _opt) {
		//dlgName: Tên dialog, sử dụng để điều khiển đóng mở dialog
		//dlgId: Id thẻ div chứa nội dung dialog
		//_url: url nội dung dialog
		//_var: biến truyền vào dialog
		//_title,_width,_height: Tiêu đề, độ rộng, chiều cao của dialog	
		dlgId = dlgName;
		if ($('body').find("#" + dlgId).length > 0) {
			//		        console.log('divHidden.html='+$('#divHidden').html());
		}
		else {
			var _html = '';
			_html += '<div id="' + dlgId + '" style="width: 100%; display: none">';
			_html += '<iframe src="" id="' + dlgId + 'ifmView"	frameborder="0"></iframe>';
			_html += '</div>';
			$('body').append(_html);
		}
		//$('#'+dlgId+'ifmView').css("width",_width-30);
		//$('#'+dlgId+'ifmView').css("height",_height-30);

		$('#' + dlgId + 'ifmView').css("width", _width);
		//$('#'+dlgId+'ifmView').css("height",_height+11);
		//			console.log('#'+dlgId+'ifmView.height'+_height);
		$('#' + dlgId + 'ifmView').height(_height);

		EventUtil.setVar("dlgVar", _var);
		$('#' + dlgId + 'ifmView').attr("src", _url + '&showMode=dlg');
		var __opts = {
			title: _title,
			theme: 'ModalBorder',
			closeOnEsc: false,
			closeOnClick: false,
			closeButton: 'title',
			overlay: true,
			zIndex: 10000,
			content: $('#' + dlgId),
			draggable: 'title',
			width: _width,
			height: _height,
			onClose: function () {
				console.log('raiseEvent onClose =' + dlgName);
				EventUtil.raiseEvent(dlgName + '_onClose', dlgName);
			}
		};
		if (_opt) {
			__opts = $.extend(__opts, _opt);
		}
		dlgPopup = new jBox('Modal', __opts);
		this.dlg[dlgName] = dlgPopup;
		return dlgPopup;
	},
	buildPopup: function (dlgName, dlgId, _title, _width, _height, _opt) {
		//popup 1 grid
		//var _dlgName = Object.create(dlgName);
		var __opts = {
			title: _title,
			closeOnEsc: false,
			closeOnClick: false,
			closeButton: 'title',
			overlay: true,
			content: $('#' + dlgId),
			draggable: 'title',
			width: _width,
			height: _height,
			onClose: function () {
				EventUtil.raiseEvent(dlgName + '_onClose', dlgName);
			}
		};
		if (_opt) {
			__opts = $.extend(__opts, _opt);
		}
		dlgPopup = new jBox('Modal', __opts);
		this.dlg[dlgName] = dlgPopup;
		return dlgPopup;
	},
	open: function (dlgName) {
		if (this.dlg[dlgName]) {
			this.dlg[dlgName].open();
		}
	},
	close: function (dlgName) {
		if (this.dlg[dlgName]) {
			this.dlg[dlgName].close();
		}
	},
	tunnel: function (fn) {
		/*
		var fnc=function(e){
			alert('e.msg='+e.msg);
			//DlgUtil.close("dlgCDDV");
		};
		*/
		fn(EventUtil.eventPool, EventUtil.varPool);
	},
	moveEvent: function (_evt, _var) {
		EventUtil.eventPool = $.extend(EventUtil.eventPool, _evt);
		EventUtil.varPool = $.extend(EventUtil.varPool, _var);
	},
	openForm: function (url, width, height, func) {
		form = window.open(url, '_blank', 'toolbar=no,location=no,scrollbars=yes,directories=0,status=yes,menubar=no,resizable=yes, copyhistory=no, width=' + width + ', height=' + height + ',left=50,top=50');
		form.callbackHandler = func;
		return form;
	}
}
//--------- ToolbarUtil ------------------------------------------------------------------------------------
var ToolbarUtil = {
	__toolbar: [],
	__iconPrefix: 'glyphicon glyphicon-',
	build: function (tbrId, ctl_ar, icoPrefix) {
		var _self = this;
		if (icoPrefix) this.__iconPrefix = icoPrefix;
		this.__toolbar[tbrId] = new _self.JsToolbar(tbrId, this.__iconPrefix);
		if (ctl_ar) {
			//console.log('1ctl_ar='+JSON.stringify(ctl_ar));
			this.__toolbar[tbrId].buildToolbar(ctl_ar);
		}
		return this.__toolbar[tbrId];
	},
	getToolbar: function (tbrId) {
		return this.__toolbar[tbrId];
	},
	JsToolbar: function (toolbarId, iconPrefix) {
		var __toolbarId = toolbarId;
		var __iconPrefix = iconPrefix;
		var __items = [];
		var __htmls = [];
		var _self = this;
		this.buildToolbar = function (ctl_ar) {
			//console.log('2ctl_ar='+JSON.stringify(ctl_ar));
			for (var i1 = 0; i1 < ctl_ar.length; i1++) {
				var ctl = ctl_ar[i1];
				//console.log('ctl['+i1+']='+JSON.stringify(ctl));
				_self.addToolbarCtl(ctl, true);
			}
			$('#' + __toolbarId).html(__htmls.join(""));
			//bindEvent();

		}
		this.addEvent = function (ctlId, evtName, evtFunc) {
			$('#' + __toolbarId + ctlId).on(evtName, evtFunc);
		}
		this.setValue = function (ctlId, _val) {
			return $('#' + __toolbarId + ctlId).val(_val);
		}
		this.getValue = function (ctlId) {
			return $('#' + __toolbarId + ctlId).val();
		}
		this.setCSS = function (ctlId, _attr, _val) {
			$('#' + __toolbarId + ctl.id).css(_attr, _val);
		}
		this.setAttr = function (ctlId, _attr, _val) {
			$('#' + __toolbarId + ctl.id).attr(_attr, _val);
		}
		this.removeAttr = function (ctlId, _attr, _val) {
			$('#' + __toolbarId + ctl.id).removeAttr(_attr);
		}

		this.addToolbarCtl = function (_ctl, _flag) {
			//ctlType,ctlId,ctlIcon,ctlText,ctlChild
			//ctlChild=[{id:'',text:'',icon:'',hlink:''}]
			var toolbar = $('#' + __toolbarId);
			//alert(toolbar);
			var _html = '';
			var _btnClass = "wd100";
			if (_ctl.cssClass) {
				_btnClass = _ctl.cssClass;
			}
			if (_ctl.type == 'button') {
				_html += '<button type="button" id="' + __toolbarId + _ctl.id + '" class="btn btn-lg btn-default ' + _btnClass + '"><span class="' + __iconPrefix + _ctl.icon + '" ></span>&nbsp;' + _ctl.text + '&nbsp;</button>';
			}
			else if (_ctl.type == 'buttongroup') {
				_html += '<div class="btn-group ' + _btnClass + '" role="group" >';
				_html += '  <button type="button" id="' + __toolbarId + _ctl.id + '" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"  style="width:100%;">';
				_html += '    <span class="' + __iconPrefix + _ctl.icon + '"></span>&nbsp;' + _ctl.text + '&nbsp;<span class="caret"></span>';
				_html += '  </button>';
				_html += '  <ul class="dropdown-menu">';
				var _cls = '';
				for (var i1 = 0; i1 < _ctl.children.length; i1++) {
					var item = _ctl.children[i1];
					if (item.group) {
						_cls = '&nbsp;&nbsp;&nbsp;&nbsp;';
						_html += '<li style="background-color:#004f9e!important" id="' + __toolbarId + item.id + '"><a href="#"><i class="' + __iconPrefix + 'folder-open"></i>&nbsp;&nbsp;' + item.text + '</a></li>';
					}
					else {
						_html += '<li id="' + __toolbarId + item.id + '"><a href="' + item.hlink + '">' + _cls + '<i class="' + __iconPrefix + item.icon + '"></i>&nbsp;&nbsp;' + item.text + '</a></li>';
					}

				}
				_html += '    </ul>';
				_html += '</div>';
			}
			else if (_ctl.type == 'textbox') {
				_html += '<input type="text" id="' + __toolbarId + _ctl.id + '" class="input-sm" placeholder="' + _ctl.text + '">';
			}
			else if (_ctl.type == 'datetime') {
				_html += '<div class="input-group inline" >';
				_html += '<input type="text" id="' + __toolbarId + _ctl.id + '" class="input-sm" data-mask="00/00/0000" placeholder="dd/MM/yyyy">';
				_html += '<span class="btn input-group-addon glyphicon glyphicon-calendar" style="display:inline-block;height:28px; width: 32px; padding: 6px 4px; top:0;" type="sCal"  onclick="NewCssCal(\'' + __toolbarId + _ctl.id + '\',\'ddMMyyyy\',\'dropdown\',false,\'24\',true)"></span>';
				_html += '</div>';
			}
			else if (_ctl.type == 'label') {
				_html += '<span id="' + __toolbarId + _ctl.id + '" class="inline navbar-right panel-title" style="padding: 4px 4px; top:0;">' + _ctl.text + '&nbsp;&nbsp;</span>';
			}
			__items.push(_ctl);
			__htmls.push(_html);
			if (!_flag) toolbar.html(__htmls.join(""));
		}
		this.clearAll = function () {
			$('#' + __toolbarId).html("");
			__currentButton = null;
			//__handler=[];
			__items = [];
			__htmls = [];
		}

	}
};


//--------- ComboUtil --------------------------------------------------------------------------------------
$.fn.filterLike = function (textbox, selectSingleMatch) {
	return this.each(function () {
		var select = this;
		var options = [];
		$(select).find('option').each(function () {
			options.push({ value: $(this).val(), text: $(this).text() });
		});
		//console.log('filterLike'+JSON.stringify(options));
		$(select).data('options', options);
		$('#' + textbox).bind('change keyup', function () {
			var options = $(select).empty().scrollTop(0).data('options');
			var search = $.trim($(this).val());
			var searchReg = '';
			for (i = 0; i < search.length; i++) {
				searchReg += '.*' + search[i];
			}
			searchReg += '.*';
			var regex = new RegExp(searchReg, 'gi');

			$.each(options, function (i) {
				var option = options[i];
				if (option.text.match(regex) !== null) {
					$(select).append(
						$('<option>').text(option.text).val(option.value)
					);
				}
			});
			if (selectSingleMatch === true &&
				$(select).children().length === 1) {
				$(select).children().get(0).selected = true;
				$(select).change();
			}
		});
	});
};
var ComboUtil = {
	showListCtl: function (_lstCtl, _data) {
		//"cboFIELD=ICD10CODE:ICD10NAME,txtField1=IDC10CODE,txtField2=ICD10NAME"
		var map_ar = _lstCtl.split(",");
		for (var i1 = 0; i1 < map_ar.length; i1++) {
			var fld_ar = map_ar[i1].split('=');
			if ($("#" + fld_ar[0]).is("select")) {
				var _flds = fld_ar[1].split(":");
				if (_flds.length == 2) {
					this.showValueText(fld_ar[0], _data[_flds[0]], _data[_flds[1]]);
				}
				else {
					$("#" + fld_ar[0]).val(_data[fld_ar[1]]);
				}
			}
			else {
				$("#" + fld_ar[0]).val(_data[fld_ar[1]]);
			}
		}
	},
	showValueText: function (cboId, _val, _text) {
		if ($("#" + cboId).is("select")) {
			if ($("#" + cboId + " option[value='" + _val + "']").length == 0) {
				$("#" + cboId).append("<option value='" + _val + "' selected >" + _text + "</option>");
			}
			else {
				$("#" + cboId + " option[value='" + _val + "']").attr("selected", "selected");
			}
		}
		else if ($("#" + cboId).is("input:text")) {
			$("#" + cboId).val(_text);
			$("#" + cboId).attr("val", _val);
		}
	},
	getComboText: function (cboId) {
		var _val = $("#" + cboId).val();
		return $("#" + cboId).find("option[value='" + _val + "']").text();
	},
	findByExtra: function (cboId, _extval, _idx) {
		if (!_idx) _idx = 0;
		console.log('ComboUtil.findByExtra ' + cboId + '=' + _extval);
		$("#" + cboId).find("option[extval" + _idx + "='" + _extval + "']").attr("selected", "selected");
		$("#" + cboId).change();
		$("#" + cboId).focus();
		//var val=$("#"+cboId).find("option[extval='"+_extval+"']").val();
		//$("#"+cboId).val(val).change();
		//$("#"+cboId).trigger("change");
	},
	selectFirstOption: function (comboid) {
		if ($('#' + comboid + ' option').length > 0) {
			$('#' + comboid + ' :nth-child(1)').prop('selected', true);
			//console.log('trigger change');
			$('#' + comboid).trigger("change");
		}
	},
	setDataToCombo: function (combo_id, value, rowSel, _defOpt) {
		var _that = this;
		_that._loadOptions(combo_id, value, rowSel, _defOpt);
	},
	_loadOptions: function (combo_id, value, rowSel, _defOpt) {
		//console.log('ComboUtil.getComboTag_loadOptions '+combo_id+'.value.length='+value.length);
		var cboList = combo_id.split(",");
		var _html = '';
		if (_defOpt && (_defOpt.value || _defOpt.value == '')) {
			_html += '<option value="' + _defOpt.value + '">' + _defOpt.text + '</option>';
		}
		console.log("----------------------_loadOptions---xxx");
		var optGroupLabel = '';
		var optGroup;
		var openGroup = false;
		console.log("----------------------value:", value);
		for (var i = 0; i < value.length; i++) {
			console.log("----------------------value:", value[i]);
			if (_defOpt.group) {
				if (optGroupLabel != value[i][2]) {
					if (openGroup) {
						_html += '</optgroup>';
					}
					openGroup = true;
					optGroupLabel = value[i][2];
					_html += '<optgroup label="' + optionLabel + '">';
				}
			}

			if (value[i].length >= 2) {
				var extval = '';
				if (_defOpt.extval) {
					//console.log('value[i].length='+value[i].length+' value[i]='+JSON.stringify(value[i]));
					var cnt = value[i].length - 2;
					//extval=' extval="'+value[i][2]+'"';
					extval = '';
					if (cnt > 0) {
						for (var i1 = 0; i1 < cnt; i1++) {
							extval += ' extval' + i1 + '="' + value[i][2 + i1] + '"';
						}
					}

				}
				var _selected = '';
				if (value[i][0] == rowSel) {
					_selected = ' selected="selected" ';
				}
				_html += '<option value="' + value[i][0] + '" ' + extval + ' ' + _selected + '>' + value[i][1] + '</option>';
			}

		}
		if (_defOpt.group && openGroup) {
			_html += '</optgroup>';
		}
		for (var i1 = 0; i1 < cboList.length; i1++) {
			console.log('_loadOptions.' + cboList[i1]);
			var elem = $('#' + cboList[i1]);
			if (!elem) continue;
			elem.empty();
			elem.append(_html);
			if (elem.attr("filterLike")) {
				var filterBy = elem.attr("filterLike");
				//console.log('filterLike='+elem.attr("id")+' sql='+filterBy);
				$('#' + cboList[i1]).filterLike(filterBy, true);
			}
		}
	},
	getComboMultiSelect: function (comboid, sql, sqlPar) {
		jsonrpc.AjaxJson.dbExecuteQuery("", sql, sqlPar, function (data) {
			var data_ar = $.parseJSON(data);
			//console.log("--------------------filter_ar: "+ JSON.stringify(data_ar));
			var options = [];
			for (var i = 0; i < data_ar.length; i++) {
				var _row = data_ar[i];
				if (_row[2])
					options.push({ "label": _row[1], "title": _row[1], "value": _row[0], "selected": true });
				else
					options.push({ "label": _row[1], "title": _row[1], "value": _row[0] });
			}
			//console.log("--------------------filter_ar1: "+ JSON.stringify(options));
			$('#' + comboid).multiselect('dataprovider', options);
			$('#' + comboid).multiselect('refresh');
		});
	},
	getMultiCheckedItems: function (comboid) {
		var brands = $('#' + comboid + ' option:selected');
		var selected = [];
		$(brands).each(function (index, brand) {
			selected.push([$(this).val()]);
		});
		return selected;
	},
	getComboTag: function (comboid, sql, sqlPar, rowSel, defOpt, sqlType, cache, callback, extOpt) {
		//extOpt={search: true, searchText: 'Tìm kiếm'};
		return this.getComboTagEx(comboid, "", sql, sqlPar, rowSel, defOpt, sqlType, cache, callback, extOpt);
	},
	getComboTagEx: function (comboid, db_name, sql, sqlPar, rowSel, defOpt, sqlType, cache, callback, extOpt) {	//getComboTag("cboId","select * from t",[],"1",{value:'',text:''},'sql','',cbFunc);
		//console.log('getComboTag='+comboid+' sql='+sql+" sqlPar="+JSON.stringify(sqlPar));
		//console.log('1sql='+sql+' sqlPar='+sqlPar);
		var vlist;
		//defer = $.Deferred();
		var _that = this;
		if (cache && cache != '') {
			//console.log('cache='+cache);
			cacheData = $.localStorage.get(cache);
		}
		else {
			cacheData = null;
		}

		if (!sql) {
			vlist = [];
			_that._loadOptions(comboid, vlist, rowSel, defOpt);
			if (callback && typeof callback == 'function') {
				callback();
			}
		}
		else if (cacheData) {
			//console.log('cacheData');
			vlist = cacheData;
			//defer.resolve(vlist);
			if (cache && cache != '') {
				$.localStorage.set(cache, vlist);
			}
			_that._loadOptions(comboid, vlist, rowSel, defOpt);
			if (callback && typeof callback == 'function') {
				callback();
			}
		}
		else {
			//console.log('NOT cacheData');
			if (callback == false) {
				var data;
				if (sqlType == 'sp') {
					data = jsonrpc.AjaxJson.dbCALL_SP_R(db_name, sql, sqlPar, []);
					//data=jsonrpc.AjaxJson.ajaxCALL_SP_O(sql,sql_par);
				}
				else {
					data = jsonrpc.AjaxJson.dbExecuteQuery(db_name, sql, sqlPar);
				}
				//console.log('sql='+sql+' sqlPar='+sqlPar+' DATA='+data);
				//vlist = $.parseJSON(data);
				if (data) {
					vlist = { result: data };
					//defer.resolve(vlist);
					if (cache && cache != '') {
						$.localStorage.set(cache, vlist.result);
					}
					_that._loadOptions(comboid, vlist.result, rowSel, defOpt);
				}
			}
			else {
				if (sqlType == 'sp') {
					jsonrpc.AjaxJson.dbCALL_SP_R(db_name, sql, sqlPar, [], function (data) {
						//console.log('sql='+sql+' sqlPar='+sqlPar+' DATA='+data);
						vlist = $.parseJSON(data);
						var _result = $.parseJSON(vlist.result);
						//defer.resolve(vlist);
						if (cache && cache != '') {
							$.localStorage.set(cache, _result);
						}

						_that._loadOptions(comboid, _result, rowSel, defOpt);
						if (callback && typeof callback == 'function') {
							callback();
						}
					});
				}
				else {
					jsonrpc.AjaxJson.dbExecuteQuery(db_name, sql, sqlPar, function (data) {
						//console.log('data='+data);
						vlist = $.parseJSON(data);
						console.log('vlist.length=' + vlist.length);
						//defer.resolve(vlist);
						if (cache && cache != '') {
							$.localStorage.set(cache, vlist);
						}
						_that._loadOptions(comboid, vlist, rowSel, defOpt);
						if (callback && typeof callback == 'function') {
							callback();
						}
					});
				}
			}
			if (extOpt) {
				//extOpt={search: true, searchText: 'Tìm kiếm', okCancelInMulti: true, selectAll: true };
				$('#' + comboid).SumoSelect(extOpt);
			}
			//
		}
		//defer.done(function(vlist) {

		//});
	},
	stringToSlug: function (str) {
		// remove accents
		var from = "àáãảạăằắẳẵặâầấẩẫậèéẻẽẹêềếểễệđùúủũụưừứửữựòóỏõọôồốổỗộơờớởỡợìíỉĩịäëïîöüûñçýỳỹỵỷ",
			to = "aaaaaaaaaaaaaaaaaeeeeeeeeeeeduuuuuuuuuuuoooooooooooooooooiiiiiaeiiouuncyyyyy";
		for (var i = 0, l = from.length; i < l; i++) {
			str = str.replace(RegExp(from[i], "gi"), to[i]);
		}

		str = str.toLowerCase()
			.trim()
			.replace(/[^a-z0-9\-]/g, '-')
			.replace(/-+/g, '-');

		return str;
	},
	custom_search_value: function (params, data) {
		params.term = params.term || '';
		term = params.term.toUpperCase().replace(/ /g, String.fromCharCode(160));
		text = data.text.toUpperCase();
		term = ComboUtil.stringToSlug(term);
		text = ComboUtil.stringToSlug(text);
		if (text.indexOf(term) > -1) {
			return data;
		}
		return false;
	},
	select2Load: function (comboid, _data, _flds, template, allowClear) {
		var _template = template || '<span class="fa {icon}"></span> {text}';
		if (!_flds) _flds = 'ID,ICON,NAME';
		var fld_ar = _flds.split(',');
		var _fncMapItem = function (item, index) {
			var newItem = new Object();
			newItem.id = item[fld_ar[0]];
			newItem.icon = item[fld_ar[1]];
			newItem.text = item[fld_ar[2]];
			return newItem;
		};

		var _allowClear = true;
		if (allowClear != undefined) {
			_allowClear = allowClear;
		}

		// console.log("-------------select2Load: ", _data);
		var rtData = _data;
		rtData = rtData.map(_fncMapItem);
		var opt = {
			matcher: function (params, data) {
				return ComboUtil.custom_search_value(params, data);
			},
			width: '100%',
			language: "vi",
			allowClear: _allowClear,
			placeholder: "",
			minimumInputLength: 0,
			data: rtData,

			templateResult: function (option, elm) {
				var rt = '';
				//				    	console.log(option);
				if (option) {
					if (!option.children) {
						rt = _template;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}
					else {
						rt = _templateGroup;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}

					return rt;
				}
			},
			templateSelection: function (option, elm, x) {
				if (elm) {
					var rt = _template;
					for (key in option) {
						rt = rt.replaceAll("{" + key + "}", option[key] || '');
					}
					return rt;
				}
			},
			// dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; }
		};
		$elm = $('#' + comboid);
		$elm.select2(opt);
	},
	select2LoadCustom: function (comboid, _data, _flds, option, template) {
		var _template = template || '<span class="fa {icon}"></span> {text}';
		if (!_flds) _flds = 'ID,ICON,NAME';
		var fld_ar = _flds.split(',');

		var _fncMapItem = function (item, index) {
			var newItem = new Object();
			if (item) {
				newItem.id = item[fld_ar[0]];
				newItem.icon = item[fld_ar[1]];
				newItem.text = item[fld_ar[2]];
			}
			return newItem;
		};

		if (!option) option = { value: "-1", text: "-- Không chọn --" };

		// console.log("-------------select2Load: ", _data);
		var rtData = [];
		rtData.push({ [fld_ar[1]]: option.value, [fld_ar[2]]: option.text });
		rtData = rtData.concat(_data);
		rtData = rtData.map(_fncMapItem);
		var opt = {
			matcher: function (params, data) {
				return ComboUtil.custom_search_value(params, data);
			},
			width: '100%',
			language: "vi",
			allowClear: true,
			placeholder: "",
			minimumInputLength: 0,
			data: rtData,

			templateResult: function (option, elm) {
				var rt = '';
				//				    	console.log(option);
				if (option) {
					if (!option.children) {
						rt = _template;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}
					else {
						rt = _templateGroup;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}

					return rt;
				}
			},
			templateSelection: function (option, elm, x) {
				if (elm) {
					var rt = _template;
					for (key in option) {
						rt = rt.replaceAll("{" + key + "}", option[key] || '');
					}
					return rt;
				}
			},
			// dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; }
		};
		$elm = $('#' + comboid);
		$elm.select2(opt);
	},
	select2LazyLoadCustom: function (comboid, objParam, _flds, initValue, tag) {
		initApiService("${request.contextPath}${n.props.crud_uri}rest_exec");
		if (!_flds) _flds = 'ID,ICON,NAME';
		var fld_ar = _flds.split(',');
		var self = apiservice.AjaxJson;
		let tag_ = tag == undefined ? false : tag;
		var initials = [];
		if (initValue !== undefined && initValue.code != '') {
			initials.push({ id: initValue.code, text: initValue.text, value: initValue.code, CODE: initValue.code });
		}
		var objJson = new Object();
		var opt = {
			tag: tag_,
			data: initials,
			width: '100%',
			language: "vi",
			allowClear: true,
			placeholder: "",
			minimumInputLength: 0,
			ajax: {
				delay: 250,
				url: self.serviceURL,
				dataType: 'json',
				type: 'POST',
				async: false,
				cache: true,
				data: function (params) {
					objParam.p_page = params.page || 1;
					objParam.p_page_length = 30;
					objParam.p_keyword = convertkeyWord(params.term);
					objJson.params = JSON.stringify(objParam);
					return objJson;
				},
				processResults: function (data, params) {
					params.page = params.page || 1;
					return {
						results: $.map(data, function (d) {
							var n = new Object;
							n.text = d[fld_ar[2]];
							n.id = d[fld_ar[0]];
							n.value = d[fld_ar[0]];
							n.code = d[fld_ar[0]];
							return n;
						}),
						pagination: {
							more: data.length > 0
						}
					};
				},
			},
		},
			$elm = $('#' + comboid);
		$elm.select2(opt);
		if (initValue !== undefined && initValue.code != '') {
			$elm.val(initValue.code).trigger('change');
		}
	},
	select2LoadCustomQuickSearch: function (comboid, _data, _flds, template, option) {
		var _template = template || '<span class="fa {icon}"></span> {text}';
		if (!_flds) _flds = 'ID,ICON,NAME';
		var fld_ar = _flds.split(',');

		var _fncMapItem = function (item, index) {
			var newItem = new Object();
			if (item) {
				if (item[fld_ar[1]] === "-1") {
					newItem.id = item[fld_ar[0]];
					newItem.icon = item[fld_ar[1]];
					newItem.text = item[fld_ar[2]];
				} else {
					newItem.id = item[fld_ar[0]];
					newItem.icon = item[fld_ar[1]];
					newItem.text = item[fld_ar[1]] + ' - ' + item[fld_ar[2]];
				}
			}
			return newItem;
		};

		// console.log("-------------select2Load: ", _data);
		var rtData = [];

		if (!option) option = { value: "-1", text: "-- Không chọn --" };
		rtData.push({ [fld_ar[1]]: option.value, [fld_ar[2]]: option.text });
		rtData = rtData.concat(_data);
		rtData = rtData.map(_fncMapItem);
		var opt = {
			width: '100%',
			language: "vi",
			allowClear: true,
			placeholder: "",
			minimumInputLength: 0,
			data: rtData,

			templateResult: function (option, elm) {
				var rt = '';
				//				    	console.log(option);
				if (option) {
					if (!option.children) {
						rt = _template;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}
					else {
						rt = _templateGroup;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}

					return rt;
				}
			},
			templateSelection: function (option, elm, x) {
				if (elm) {
					var rt = _template;
					for (key in option) {
						rt = rt.replaceAll("{" + key + "}", option[key] || '');
					}
					return rt;
				}
			},
			// dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; }
		};
		$elm = $('#' + comboid);
		$elm.select2(opt);
	},
	select2LoadCustomQuickSearchEthnic: function (comboid, _data, _flds, template) {
		var _template = template || '<span class="fa {icon}"></span> {text}';
		if (!_flds) _flds = 'ID,ICON,NAME,OTHERNAME';
		var fld_ar = _flds.split(',');

		var _fncMapItem = function (item, index) {
			var newItem = new Object();
			if (item) {
				if (item[fld_ar[1]] === "-1") {
					newItem.id = item[fld_ar[0]];
					newItem.icon = item[fld_ar[1]];
					newItem.text = item[fld_ar[2]];
				} else {
					newItem.id = item[fld_ar[0]];
					newItem.icon = item[fld_ar[1]];
					if (item[fld_ar[3]] == '' || item[fld_ar[3]] == null) {
						newItem.text = item[fld_ar[2]];
					} else {
						newItem.text = item[fld_ar[2]] + ' - ' + item[fld_ar[3]];
					}
				}
			}
			return newItem;
		};

		// console.log("-------------select2Load: ", _data);
		var rtData = [];
		rtData.push({ [fld_ar[1]]: "-1", [fld_ar[2]]: "-- Không chọn --" });
		rtData = rtData.concat(_data);
		rtData = rtData.map(_fncMapItem);
		var opt = {
			width: '100%',
			language: "vi",
			allowClear: true,
			placeholder: "",
			minimumInputLength: 0,
			data: rtData,

			templateResult: function (option, elm) {
				var rt = '';
				//				    	console.log(option);
				if (option) {
					if (!option.children) {
						rt = _template;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}
					else {
						rt = _templateGroup;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}

					return rt;
				}
			},
			templateSelection: function (option, elm, x) {
				if (elm) {
					var rt = _template;
					for (key in option) {
						rt = rt.replaceAll("{" + key + "}", option[key] || '');
					}
					return rt;
				}
			},
			// dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; }
		};
		$elm = $('#' + comboid);
		$elm.select2(opt);
	},
	select2LazyLoad: function (comboid, _strSQL, sql_par, _flds, template, _opt, _isGroup) {
		//var _template = template || "<b style='margin-right:8px;'>{id}</b> {text}";
		var _template = template || '<span class="fa {icon}"></span> {text}';
		var _templateGroup = "<b style='margin-right:8px;color:#3c8dbc'><i class='fa fa-folder'></i> {text}</b> ";
		var MAX_ROW = 30;
		if (!_flds) _flds = 'ID,NAME';
		var fld_ar = _flds.split(',');
		var _fncMapItem = function (item, index) {
			var newItem = new Object();
			newItem.id = item[fld_ar[0]];
			newItem.text = item[fld_ar[1]];
			for (var i1 = 2; i1 < fld_ar.length; i1++) {
				newItem[fld_ar[i1]] = item[fld_ar[i1]];
			}
			return newItem;
		};
		var _fncSetAttr = function (item, elem) {
			for (var i1 = 2; i1 < fld_ar.length; i1++) {
				elem.attr(fld_ar[i1], item[fld_ar[i1]]);
			}
		};
		var opt = {
			width: '100%',
			language: "vi",
			allowClear: true,
			placeholder: "",
			minimumInputLength: 0,
			ajax: {
				delay: 250,
				url: jsonrpc.AjaxJson.serviceURL,
				dataType: 'json',
				data: function (params) {
					var search_key = params.term || '';
					var _par = sql_par.slice(0);
					search_key = replaceAll(search_key.trim().toUpperCase(), ' ', '%');
					_par.push({ "name": "[0]", "value": search_key });

					_postdata = {
						"func": "ajaxExecuteQueryPaging",
						"uuid": jsonrpc.AjaxJson.uuid,
						"params": [RSUtil.encode(_strSQL)],
						"options": _par
					};
					return { postData: JSON.stringify(_postdata), page: params.page || 1, rows: MAX_ROW };
				},
				processResults: function (data, params) {
					params.page = params.page || 1;
					var rt = data.rows;
					//console.log('1.rt='+JSON.stringify(rt));

					var group_list = {};
					if (!_isGroup) {
						if (_fncMapItem && typeof _fncMapItem == 'function') {
							rt = rt.map(_fncMapItem);
						}
					}
					else {
						for (var i1 = 0; i1 < rt.length; i1++) {
							var _row = rt[i1];
							var parent_id = _row['PARENT_ID'];
							var parent_name = _row['PARENT_NAME'];
							if (!group_list[parent_id]) {
								group_list[parent_id] = { text: parent_name, children: [] };
							}
							//				        			  console.log('1._row[fld_ar[0]]='+_row[fld_ar[0]]);
							if (_row[fld_ar[0]]) {
								var newItem = {};
								newItem.id = _row[fld_ar[0]];
								newItem.text = _row[fld_ar[1]];
								group_list[parent_id].children.push(newItem);
							}
						}
						rt = [];
						for (_key in group_list) {
							rt.push(group_list[_key]);
						}
					}

					//console.log('2.rt='+JSON.stringify(rt));
					/*
					rt= [{
							text: 'Cha 1', children: [
								  {id: '1.1', text: 'Con 1.1'},
								  {id: '1.2', text: 'Con 1.2'}
							  ]
							  },
							  {
							  text: 'Cha 2', children: [
								  {id: '2.1', text: 'Con 2.1'},
								  {id: '2.2', text: 'Con 2.2'}
							   ]
						   }];
					*/
					return {
						results: rt,
						pagination: {
							more: params.page < data.total
						}
					};
				},
				cache: true
			},
			templateResult: function (option, elm) {
				var rt = '';
				console.log(option);
				if (option) {
					if (!option.children) {
						rt = _template;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}
					else {
						rt = _templateGroup;
						for (key in option) {
							rt = rt.replaceAll("{" + key + "}", option[key] || '');
						}
					}

					return rt;
				}
			},
			templateSelection: function (option, elm, x) {
				if (elm) {
					var rt = _template;
					for (key in option) {
						rt = rt.replaceAll("{" + key + "}", option[key] || '');
					}
					return rt;
				}
			},
			dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; }
		};
		if (_opt) {
			opt = $.extend(true, {}, opt, _opt);
		}
		$elm = $('#' + comboid);
		$elm.select2(opt);
		if (opt.customClass && $elm.hasClass(opt.customClass)) {
			$elm.next().addClass(opt.customClass);
		}
	},
	initComboGrid: function (cboId, _sql, _sql_par, _wd, _col, _selfnc) {
		//comboGrid_init	
		var _self = this;
		var ATTRIB_LABEL = 0;
		var ATTRIB_NAME = 1;
		var ATTRIB_WIDTH = 2;
		var ATTRIB_FORMAT = 3;
		var ATTRIB_HIDDEN = 4;
		var ATTRIB_ALIGN = 5;

		var cbo_url = this.loadGridBySql(_sql, _sql_par);
		var arHdr = _col.split(";");
		var _model = [];

		for (var j = 0; j < arHdr.length; j++) {
			var colAttr = arHdr[j].split(",");
			var colInfo = new Object();
			colInfo.label = colAttr[ATTRIB_LABEL];
			colInfo.columnName = colAttr[ATTRIB_NAME];
			colInfo.width = colAttr[ATTRIB_WIDTH];
			if (colAttr[ATTRIB_HIDDEN] == "t") {
				colInfo.hidden = true;
			}

			if (colAttr[ATTRIB_ALIGN] == "l") {
				colInfo.align = "left";
			}
			else if (colAttr[ATTRIB_ALIGN] == "r") {
				colInfo.align = "right";
			}
			else if (colAttr[ATTRIB_ALIGN] == "c") {
				colInfo.align = "center";
			}
			/*
			if (colAttr[ATTRIB_FORMAT] && colAttr[ATTRIB_FORMAT] != "0") {			
				colInfo.formatter = colAttr[ATTRIB_FORMAT];			
			}
			*/
			_model.push(colInfo);
		}
		//console.log("model="+JSON.stringify(_model));
		//"txtField1=IDC10CODE,txtField2=ICD10NAME,"
		//cboFIELD=ICD10CODE:ICD10NAME
		var cbFunc = null;
		if (typeof _selfnc == "function") {
			cbFunc = _selfnc;
		}
		else {
			cbFunc = function (event, ui) {
				_self.showListCtl(_selfnc, ui.item);
				return false;
			};
		}
		var cbo_opt = {
			url: cbo_url,
			debug: true,
			width: _wd,
			colModel: _model,
			select: cbFunc
		};

		$("#" + cboId).attr("exttype", "cbg");
		//						console.log("#"+cboId+".exttype="+$("#"+cboId).attr("exttype"));
		$("#" + cboId).combogrid(cbo_opt);
	},

	loadGridBySql: function (_gridSQL, ___sqlParam) {
		//comboGrid_loadGridBySql	
		//			console.log('_sqlParam='+JSON.stringify(___sqlParam));
		if (___sqlParam == undefined) ___sqlParam = [];
		var _data = {
			"func": "ajaxExecuteQueryPaging",
			"uuid": jsonrpc.AjaxJson.uuid,
			"params": [_gridSQL],
			"options": ___sqlParam
		};
		var _postdata = JSON.stringify(_data);
		//console.log('_postdata='+_postdata);
		_postdata = encodeURIComponent(_postdata);
		//console.log('_postdata='+_postdata);
		return RestInfo.base_url + '?func=doComboGrid&postData=' + _postdata;
	}


};
ComboUtil.init = ComboUtil.initComboGrid;


//--------- TreeUtil ---------------------------------------------------------------------------------------
var TreeUtil = {
	_makeTree: function (options, data) {
		var children, e, id, o, pid, temp, _i, _len, _ref;
		id = options.id || "id";
		pid = options.parentid || "parentid";
		children = options.children || "children";
		temp = {};
		o = [];
		_ref = data;

		for (_i = 0, _len = _ref.length; _i < _len; _i++) {
			//e = _ref[_i];
			e = {};
			for (var _property in _ref[_i]) {
				if (_ref[_i].hasOwnProperty(_property)) {
					e[_property.toLowerCase()] = _ref[_i][_property];
				}
			}
			e[children] = [];
			temp[e[id]] = e;
			if (temp[e[pid]] != null) {
				temp[e[pid]][children].push(e);
			} else {
				o.push(e);
			}
		}
		//console.log("reload_newData="+JSON.stringify(o));
		return o;
	},
	init: function (treeId, _checkbox, _selectMode, _dnd) {
		/*
		 $("#selector").fancytree({
		activeVisible: true, // Make sure, active nodes are visible (expanded).
		aria: false, // Enable WAI-ARIA support.
		autoActivate: true, // Automatically activate a node when it is focused (using keys).
		autoCollapse: false, // Automatically collapse all siblings, when a node is expanded.
		autoScroll: false, // Automatically scroll nodes into visible area.
		clickFolderMode: 4, // 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
		checkbox: false, // Show checkboxes.
		debugLevel: 2, // 0:quiet, 1:normal, 2:debug
		disabled: false, // Disable control
		focusOnSelect: false, // Set focus when node is checked by a mouse click
		escapeTitles: false, // Escape `node.title` content for display
		generateIds: false, // Generate id attributes like <span id='fancytree-id-KEY'>
		idPrefix: "ft_", // Used to generate node id´s like <span id='fancytree-id-<key>'>.
		icon: true, // Display node icons.
		keyboard: true, // Support keyboard navigation.
		keyPathSeparator: "/", // Used by node.getKeyPath() and tree.loadKeyPath().
		minExpandLevel: 1, // 1: root node is not collapsible
		quicksearch: false, // Navigate to next node by typing the first letters.
		selectMode: 2, // 1:single, 2:multi, 3:multi-hier
		tabindex: "0", // Whole tree behaves as one single control
		titlesTabbable: false // Node titles can receive keyboard focus
	});
		 */
		var t_opt = {
			extensions: ["filter"],
			checkbox: _checkbox,
			selectMode: 2,
			autoScroll: true,
			debugLevel: 0,
			folder: true,
			quicksearch: true,
			source: [],
			filter: {
				autoApply: true,
				// autoExpand: true,
				mode: "hide"
			}

			/*
		  ,select: function(event, data) {
			  nguoidung = $.map(data.tree.getSelectedNodes(), function(node) {
					console.log('select='+node.key);
				  return node.key;
			  });
		  }
		  */
		};
		if (_dnd) {
			t_opt.extensions = ["dnd", "edit", "filter"];
			t_opt.dnd = {
				autoExpandMS: 400,
				focusOnClick: true,
				preventVoidMoves: true, // Prevent dropping nodes 'before self', etc.
				preventRecursiveMoves: true, // Prevent dropping nodes on own descendants
				dragStart: _dnd.dragStart,
				dragEnter: _dnd.dragEnter,
				dragDrop: _dnd.dragDrop
			};
		}
		if (_selectMode) {
			t_opt.selectMode = _selectMode;
		}
		//				console.log('fcTree_init='+treeId);
		$("#" + treeId).fancytree(t_opt);
		var tree = $("#" + treeId).fancytree("getTree");
		return tree;

	},
	load: function (treeId, _data, _bExpand) {
		var _self = this;
		if (_data != undefined) {
			/**/
			$(function () {
				var _newData = _self._makeTree({ id: "key", parentid: "parent" }, _data);
				var tree = $("#" + treeId).fancytree("getTree");
				//						console.log("reload_newData="+JSON.stringify(_newData));
				tree.reload(_newData);
				if (_bExpand) {
					$("#" + treeId).fancytree("getRootNode").visit(function (node) {
						node.setExpanded(true);
					});
				}
			});

		}
		else {
			console.log('fcTree_load._data=undefined');
		}
	}
};
//--------- UploadUtil ---------------------------------------------------------------------------------------
var UploadUtil = {
	upload: function (_formId, _param, _succFnc) {
		$("#param").value = _param;
		var opts = {
			url: "../upload/saveFile.jsp",
			type: "post",
			dataType: "json",
			success: function (data) {
				console.log('upload data.id=' + data.id);
				$('#imgMEDIA_ID').attr("src", data.url);
			}
		};
		if (typeof _succFnc == "function") {
			opts.success = _succFnc;
		}

		$("#" + _formId).ajaxForm(opts).submit();
	},
	uploadBase64: function (_data, _fileType, _succFnc) {
		//var _data='data:audio/wav;base64,UklGRixAKABXQVZFZm10IBAAAAABAAIARKwAABCxAgAEABAAZGF0Y�EJ4AngCR4KHgpYClgKcApwCnwKfAqGCoYKcgpyCkEKQQoSChIK3QndCZcJlwljCWMJWglaCQ==';
		//var rt=jsonrpc.AjaxJson.uploadMediaBase64(_data,'wav');
		if (typeof _succFnc == "function") {
			jsonrpc.AjaxJson.uploadMediaBase64(_data, _fileType, _succFnc);
		}
		else {
			var rt = jsonrpc.AjaxJson.uploadMediaBase64(_data, _fileType);

			return { id: rt, url: '../upload/getdata.jsp?id=' + rt };
		}

	},
	deleteMedia: function (_mediaId, _succFnc) {
		//var _data='data:audio/wav;base64,UklGRixAKABXQVZFZm10IBAAAAABAAIARKwAABCxAgAEABAAZGF0Y�EJ4AngCR4KHgpYClgKcApwCnwKfAqGCoYKcgpyCkEKQQoSChIK3QndCZcJlwljCWMJWglaCQ==';
		//var rt=jsonrpc.AjaxJson.uploadMediaBase64(_data,'wav');
		if (typeof _succFnc == "function") {
			jsonrpc.AjaxJson.deleteMedia(_mediaId, _succFnc);
		}
		else {
			var rt = jsonrpc.AjaxJson.deleteMedia(_mediaId);

			return rt;
		}

	}
};

var Base64 = {

	// private property
	_keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="

	// public method for encoding
	, encode: function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;

		input = Base64._utf8_encode(input);

		while (i < input.length) {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);

			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;

			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			}
			else if (isNaN(chr3)) {
				enc4 = 64;
			}

			output = output +
				this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
				this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
		} // Whend

		return output;
	} // End Function encode


	// public method for decoding
	, decode: function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;

		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < input.length) {
			enc1 = this._keyStr.indexOf(input.charAt(i++));
			enc2 = this._keyStr.indexOf(input.charAt(i++));
			enc3 = this._keyStr.indexOf(input.charAt(i++));
			enc4 = this._keyStr.indexOf(input.charAt(i++));

			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;

			output = output + String.fromCharCode(chr1);

			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}

			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}

		} // Whend

		output = Base64._utf8_decode(output);

		return output;
	} // End Function decode


	// private method for UTF-8 encoding
	, _utf8_encode: function (string) {
		var utftext = "";
		string = string.replace(/\r\n/g, "\n");

		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);

			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if ((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}

		} // Next n

		return utftext;
	} // End Function _utf8_encode

	// private method for UTF-8 decoding
	, _utf8_decode: function (utftext) {
		var string = "";
		var i = 0;
		var c, c1, c2, c3;
		c = c1 = c2 = 0;

		while (i < utftext.length) {
			c = utftext.charCodeAt(i);

			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if ((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i + 1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i + 1);
				c3 = utftext.charCodeAt(i + 2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}

		} // Whend

		return string;
	} // End Function _utf8_decode

}
