
/** 二重サブミット防止用の関数宣言 */
(function() {
	$.fn.disableOnSubmit = function() {
		var buttons = $("input[type=submit],input[type=button],input[type=reset],button", this);
		buttons.on("click",function() {
			var hidden = $("<input />").
				prop("type", "hidden").
				prop("name", $(this).prop("name")).
				prop("value", $(this).prop("value"));
	        $(this).after(hidden);
		});
		$(this).on("submit",function() {
			buttons.prop("disabled", true);
		});
		return this;
	}
})();

$("form").on("submit", function() {
	  if ($(this).data("submitted")) {
	    return false;
	  }
	  $(this).data("submitted", true);

	  var self = this;

	  // タイムアウト処理
	  setTimeout(function() {
	    $(self).data("submitted", false)
	  }, 10000);
	});

/** ページング処理 */
function paging(a,b,c,d) {
  if ($("#jsi-pn-disabled").val() == 1){
    event.preventDefault();
    return false;
  }
  $("#jsi-pn-disabled").val(1);
  if(a){ $("#jsi-pn").val(a);}
  if(b){$("#jsi-sort-key").val(b);}
  if(c){$("#jsi-sort-direction").val(c);}
  $("form").prop('action',Globals.contextroot+d).submit();
};

/** 初期化処理 */
$(function(){

  $(".submenu > a").click(function(e) {
    e.preventDefault();
    var $li = $(this).parent("li");
    var $ul = $(this).next("ul");

    if ($li.hasClass("open")) {
      $ul.slideUp(350);
      $li.removeClass("open");
    } else {
      $(".nav > li > ul").slideUp(350);
      $(".nav > li").removeClass("open");
      $ul.slideDown(350);
      $li.addClass("open");
    }
  });
});

/** DatePicker設定 */
$('.datepicker').datepicker({
  title: "",
  language: "ja",
  autoclose: true,
  todayHighlight: true
});

/** 連動プルダウンNamespace */
var LinkagePulldown = {};

/**
 * 2連プルダウンの連動設定を行う
 * @param firstItemId 親項目ID
 * @param secondItemId 子項目ID
 * @param linkageAttribute 子項目のOPTIONに定義している連動紐付け属性名
 */
LinkagePulldown.linkageDoublePulldown = function(firstItemId, secondItemId, linkageAttribute) {
  var firstItem = $("#" + firstItemId);

  firstItem.on("change", function() {
    LinkagePulldown._settingLinkage(firstItem.val(), secondItemId, linkageAttribute);
  })
}

/**
 * 3連プルダウンの連動設定を行う
 * @param firstItemId 親項目ID
 * @param secondItemId 子項目ID
 * @param thirdItemId 孫項目ID
 * @param firstLinkageAttribute 子・孫項目のOPTIONに定義している親項目との連動紐付け属性名
 * @param secondLinkageAttribute 孫項目のOPTIONに定義している子項目との連動紐付け属性名
 */
LinkagePulldown.linkageTriplePulldown = function(firstItemId, secondItemId, thirdItemId, firstLinkageAttribute, secondLinkageAttribute) {
  var firstItem = $("#" + firstItemId);
  var secondItem = $("#" + secondItemId);

  // 親項目の変更時
  firstItem.on("change", function() {
    var firstItemValue = firstItem.val();

    LinkagePulldown._settingLinkage(firstItemValue, secondItemId, firstLinkageAttribute);
    LinkagePulldown._settingLinkage(firstItemValue, thirdItemId, firstLinkageAttribute);
  });

  // 子項目の変更時
  secondItem.on("change", function() {
    LinkagePulldown._settingLinkage(secondItem.val(), thirdItemId, secondLinkageAttribute);
  });
}

/**
 * 連動紐付け設定を行う
 * @param parentValue 親項目の選択値
 * @param childId 子項目ID
 * @param linkageAttribute 連動紐付け属性名
 */
LinkagePulldown._settingLinkage = function(parentValue, childId, linkageAttribute) {
  $("#" + childId).children("option").each(function() {
    var option = $(this);

    if (!parentValue) {
      // 親項目が未選択の場合、子項目は全て選択可能
      option.show();
    } else {
      var linkageValue = option.attr(linkageAttribute);

      if (parentValue == linkageValue) {
        option.show();
      } else {
        !linkageValue ? option.show().prop("selected", true) : option.hide();
      }
    }
  });
}
