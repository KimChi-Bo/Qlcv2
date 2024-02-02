var CTL_DATA_TYPE = 0;
var CTL_CONTROL_TYPE = 1;
var CTL_COLUMN_NAME = 2;
var CTL_COLUMN_ATR = 3;
var CTL_COLUMN_DEFAULT = 4;
var FormUtil = {
	setEnabled: function (_ena, _dis) {
		for (var i = 0; i < _ena.length; i++) {
			$("#" + _ena[i]).attr('disabled', false);
		}
		for (var i = 0; i < _dis.length; i++) {
			$("#" + _dis[i]).attr('disabled', true);
		}
	},
	setEnabledCtl: function (_containerId, _ena, _dis) {
		var ctl = '';
		if (_containerId)
			ctl = '#' + _containerId + ' ';
		for (var i = 0; i < _ena.length; i++) {
			$(ctl + "#" + _ena[i]).attr('disabled', false);
		}
		for (var i = 0; i < _dis.length; i++) {
			$(ctl + "#" + _dis[i]).attr('disabled', true);
		}
	},

	setEnabledForm: function (_containerId, _prefix, _enable) {
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			_container = $(document);
		}
		else {
			_container = $('#' + _containerId);
		}
		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["txt", "cbo", "hid", "chk", "lbl"];//["txt","cbo","hid","chk","lbl"]
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = _container.find("input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "'],label[id^='" + _prefix + _type + "']");
			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				$(ctl).attr('disabled', !_enable);
			}
		}

		return true;
	}, loadForm: function (_div, _url) {
		$elm = $("#" + _div);
		if (!_url) _url = $elm.attr('data_url');

		if (!_loadedPanel[_div] || moment().diff(_loadedPanel[_div]) > _reloadInterval * 1000) { //


			console.log("_urlx", _url);
			$elm.load(_url, function () { //calback function
				_loadedPanel[_div] = moment();
				$("*#ctrlForm").hide();
				$("#dLoading111").addClass("hidden");
				$elm.attr('loaded', true);
				console.log("done");
				$(document).find('*').removeAttr('placeholder');
				console.log("remove");
			});
		}
	},
	setTitle: function (_name, _title) {
		var _container = $(document);
		var ctl_ar = _container.find("div[data-i18n='" + _name + "'],label[data-i18n='" + _name + "']");
		for (var i = 0; i < ctl_ar.length; i++) {
			var ctl = ctl_ar[i];
			//var fldName=ctl.id.substring(3);
			//console.log('$(ctl[0]).tagName='+$(ctl[0]).tagName);
			if ($(ctl).is('div')) {
				$(ctl).html(_title);
			}
			else if ($(ctl).is('label')) {
				ctl.textContent = _title;
			}
			else {
				//ctl.val("");
				ctl.value = _title;
			}

		}
	},
	clearForm: function (_containerId, _prefix) {
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			_container = $(document);
		}
		else {
			_container = $('#' + _containerId);
		}
		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["txt", "cbo", "hid", "chk", "lbl"];//["txt","cbo","hid","chk","lbl"]
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = _container.find("input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "'],label[id^='" + _prefix + _type + "']");

			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				var fldName = ctl.id.substring(3);
				//console.log('fldName='+fldName);
				if (_type == 'chk') {
					ctl.checked = false;
				}
				else if (_type == 'lbl') {
					ctl.textContent = '';
				}
				else {
					//ctl.val("");
					ctl.value = "";
				}

			}
		}

		return true;
	},
	// unescape: function(strIn) {
	// 	var strVal=strIn;
	// 	strVal=strVal.replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/&amp;/g,'&').replace(/&quot;/g, '"');
	// 	strVal=strVal.replace(/&LT;/g,'<').replace(/&GT;/g,'>').replace(/&AMP;/g,'&').replace(/&QUOT;/g, '"').replace(/&#36;/g,'$');//
	// 	return strVal;
	// },
	escape: function (strIn) {
		//console.log('strIn='+strIn);
		var strVal = strIn.trim();
		strVal = strVal.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/\$/g, '&#36;');//
		//console.log('escape strIn='+strVal);
		return strVal;
	},
	setObjectToForm: function (_containerId, _prefix, objData) {
		if (objData == null) objData = new Object();
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			//console.log('setObjectToForm._container=document');
			_container = $(document);
		}
		else {
			//console.log('setObjectToForm._container='+_containerId);
			_container = $('#' + _containerId);
		}
		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["cbo", "txt", "hid", "chk", "lbl", "mul", "tbl", "ckb", "rd"];
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = [];

			ctl_ar = _container.find("[id^='" + _prefix + _type + "'],input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "'],label[id^='" + _prefix + _type + "'],table[id^='" + _prefix + _type + "']");
			// console.log("setObjectToForm="+_prefix+_type+' ctl_ar.length='+ctl_ar.length);
			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				var fldName = ctl.id.substring((_prefix + _type).length);

				// console.log('fldName='+fldName+';type='+_type);
				// console.log("-----------data object: ", objData);
				if (objData[fldName] != null) {
					if (_type == 'rd') {
						//console.log("-----------data object: ", objData[fldName]);
						$('#' + ctl.id).find('[value=' + objData[fldName] + ']').prop('checked', true).change();
					}

					if (_type == 'chk') {

						if (objData[fldName] == 1) {
							ctl.checked = true;
						}
						else {
							ctl.checked = false;
						}
					}
					if (_type == 'ckb') {

						if (objData[fldName] == 1) {
							ctl.checked = true;
						}
						else {
							ctl.checked = false;
						}
					}
					else if (_type == 'lbl') {
						ctl.textContent = objData[fldName];//this.unescape(objData[fldName.toUpperCase()]);
					}
					else if (_type == 'tbl') {
						var table_item = $('#' + ctl.id);
						var data_apply = objData[fldName];
						if (table_item != null && data_apply != null) {
							table_item.bootstrapTable({ data: data_apply });
						}
					}
					else if (_type == 'mul') {
						var _pid = objData[fldName];
						var val_ar = _pid.split(",");
						$(ctl).val(val_ar);
					}
					else if (_type == 'cbo') {
						ctl.value = objData[fldName];
						$(ctl).trigger("change");
					}
					else {
						var strVal = objData[fldName] + "";

						strVal = strVal.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&');
						//strVal=strVal.replace(/&LT;/g,'<').replace(/&GT;/g,'>').replace(/&AMP;/g,'&');
						ctl.value = strVal;
						//ctl.value = objData[fldName.toUpperCase()];
						//if(_type=='cbo')
						//$(ctl).trigger("change");

					}
					//console.log($(ctl).attr("id")+".exttype="+$(ctl).attr("exttype"));
					if ($(ctl).attr("exttype") == 'cbg') {
						//console.log($(ctl).attr("id")+".exttype="+$(ctl).attr("exttype")+" objData="+objData[fldName.toUpperCase()]);
						var _data = objData[fldName];
						if (_data != undefined && _data != null && _data != '')
							$(ctl).combogrid("setValue", objData[fldName]);
					}
				}
			}
		}

		return true;
	},

	setFormToObject: function (_containerId, _prefix, objData, _esc) {
		if (objData == null) objData = new Object();
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			_container = $(document);
		}
		else {
			_container = $('#' + _containerId);
		}


		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["txt", "cbo", "hid", "chk", "rad", "mul", "lbl", "tbl", "ckb"];
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = [];
			ctl_ar = _container.find("input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "'],label[id^='" + _prefix + _type + "'],table[id^='" + _prefix + _type + "']");

			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				var fldName = ctl.id.substring((_prefix + _type).length);
				//console.log('fldName='+fldName);
				if (_type == 'chk') {
					objData[fldName] = ($(ctl).is(":checked") ? '1' : '0');
				}
				if (_type == 'ckb') {
					objData[fldName] = ($(ctl).is(":checked") ? '1' : '0');
				}
				else if (_type == 'rad') {
					objData[fldName] = $.find("[name='" + ctl.name + "']:checked")[0].value;
				}
				else if (_type == 'cbo') {
					try {
						objData[fldName] = $(ctl).val() == null ? "" : $(ctl).val().trim();
					} catch {
						objData[fldName] = $(ctl).val() == null ? "" : $(ctl).val().toString().trim();
					}

					if ($(ctl).attr("reffld")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld] = $(ctl).find("option:selected").text().trim();
					}
				}
				else if (_type == 'mul') {
					var _pid = '';
					var val_ar = [];
					$('#' + ctl.id + ' :selected').each(function (i, sel) {
						val_ar.push($(sel).val());
					});
					_pid = val_ar.join(",");
					objData[fldName] = _pid;
				}
				else if (_type == 'tbl') {
					var tableSelected = $('#' + ctl.id);
					if (tableSelected != null) {
						var jsonString = JSON.stringify(tableSelected.bootstrapTable('getData'));
						objData[fldName] = jsonString;
					}
				}
				else if (_type == 'lbl') {
					// console.log('ctl.id=',ctl.textContent);
					var strVal = ctl.textContent.trim();
					//strVal=strVal.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
					if (!_esc) strVal = this.escape(strVal);
					objData[fldName] = strVal;
					if ($(ctl).attr("reffld") && $(ctl).attr("textContent")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld] = ctl.textContent.trim();
					}
				}
				else {
					// console.log('ctl.id='+ctl.id);
					// console.log('ctl.val='+$(ctl).val());
					var strVal = '';
					if ($(ctl).val()) {
						var strVal = $(ctl).val().trim();
						//strVal=strVal.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
						if (!_esc) strVal = this.escape(strVal);
					}
					objData[fldName] = strVal;
					if ($(ctl).attr("reffld") && $(ctl).attr("val")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld] = $(ctl).attr("val").trim();
					}
				}

			}
		}
	},
	setFormToObject_Custom: function (_containerId, _prefix, objData, _esc) {
		if (objData == null) objData = new Object();
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			_container = $(document);
		}
		else {
			_container = $('#' + _containerId);
		}


		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["txt", "cbo", "hid", "chk", "rad", "mul", "lbl", "tbl", "ckb"];
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = [];
			ctl_ar = _container.find("input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "'],label[id^='" + _prefix + _type + "'],table[id^='" + _prefix + _type + "']");

			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				var fldName = ctl.id.substring((_prefix + _type).length);
				//console.log('fldName='+fldName);
				if (_type == 'chk') {
					objData[fldName.toUpperCase()] = ($(ctl).is(":checked") ? '1' : '0');
				}
				if (_type == 'ckb') {
					objData[fldName.toUpperCase()] = ($(ctl).is(":checked") ? '1' : '0');
				}
				else if (_type == 'rad') {
					objData[fldName.toUpperCase()] = $.find("[name='" + ctl.name + "']:checked")[0].value;
				}
				else if (_type == 'cbo') {
					try {
						objData[fldName.toUpperCase()] = $(ctl).val() == null ? "" : $(ctl).val();
					} catch {
						objData[fldName.toUpperCase()] = $(ctl).val() == null ? "" : $(ctl).val().toString();
					}

					if ($(ctl).attr("reffld")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld.toUpperCase()] = $(ctl).find("option:selected").text().trim();
					}
				}
				else if (_type == 'mul') {
					var _pid = '';
					var val_ar = [];
					$('#' + ctl.id + ' :selected').each(function (i, sel) {
						val_ar.push($(sel).val());
					});
					_pid = val_ar.join(",");
					objData[fldName.toUpperCase()] = _pid;
				}
				else if (_type == 'tbl') {
					var tableSelected = $('#' + ctl.id);
					if (tableSelected != null) {
						var jsonString = JSON.stringify(tableSelected.bootstrapTable('getData'));
						objData[fldName.toUpperCase()] = jsonString;
					}
				}
				else if (_type == 'lbl') {
					// console.log('ctl.id=',ctl.textContent);
					var strVal = ctl.textContent.trim();
					//strVal=strVal.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
					if (!_esc) strVal = this.escape(strVal);
					objData[fldName.toUpperCase()] = strVal;
					if ($(ctl).attr("reffld") && $(ctl).attr("textContent")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld.toUpperCase()] = ctl.textContent.trim();
					}
				}
				else {
					// console.log('ctl.id='+ctl.id);
					// console.log('ctl.val='+$(ctl).val());
					var strVal = '';
					if ($(ctl).val()) {
						var strVal = $(ctl).val().trim();
						//strVal=strVal.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
						if (!_esc) strVal = this.escape(strVal);
					}
					objData[fldName.toUpperCase()] = strVal;
					if ($(ctl).attr("reffld") && $(ctl).attr("val")) {
						var reffld = $(ctl).attr("reffld");
						objData[reffld.toUpperCase()] = $(ctl).attr("val").trim();
					}
				}

			}
		}
	},
	check_length: function (_containerId, _prefix) {
		var _container = null;
		if ((_containerId == null) || (_containerId.length <= 0)) {
			_container = $(document);
		}
		else {
			_container = $('#' + _containerId);
		}
		if ((_container == null) || (_container.length <= 0)) return;
		var ctl_type = ["txt", "mul"];//["txt","cbo","hid","chk","lbl","mul"]
		for (var j = 0; j < ctl_type.length; j++) {
			_type = ctl_type[j];
			var ctl_ar = _container.find("input[id^='" + _prefix + _type + "'],textarea[id^='" + _prefix + _type + "'],select[id^='" + _prefix + _type + "']");
			for (var i = 0; i < ctl_ar.length; i++) {
				var ctl = ctl_ar[i];
				var val = ctl.value;
				// var fldName=ctl.id.substring(3);
				if (val.length > 0) {
					return '1';
				}
			}
		}
		return '0';
	},
	enableLogger: function () {
		var oldConsoleLog = null;
		if (oldConsoleLog == null)
			return;
		window['console']['log'] = oldConsoleLog;
	},
	disableLogger: function () {
		var oldConsoleLog = null;
		oldConsoleLog = console.log;
		window['console']['log'] = function () { };
	}

};