var lastOpenDiv;

$(window).bind('click', (function () {
	if (lastOpenDiv) {
		lastOpenDiv.remove();
		lastOpenDiv = null;
	}
}));

$(window).keyup(function (e) {
	if (e.keyCode == 27 && lastOpenDiv) {
		lastOpenDiv.remove();
		lastOpenDiv = null;
	}
});

(function ($) {
	$.fn.showDtPopup = function (configType, yearId, monthId, dayId, parentId) {
		var defaults = {
			yearInput: null,
			monthInput: null,
			dayInput: null,
			divPopup: null,
			dtTable: null,
			selectYear: null,
			selectMonth: null,
			trFirst: null,
			tdFirst: null,
			trWeek: null,
			dayHandlerResult: null,
			dayOfRow: null,
			numberOfRow: null,
			dtTBody: null,
			dtTHead: null,
			calendars: {
				jalali: {
					direction: "rtl",
					years: function () {
						var currentDate = new Date();
						var dg = [currentDate.getFullYear(), currentDate.getMonth() + 1, currentDate.getDate()];
						var jalaliCurrent = Date.jalaliConverter.gregorianToJalali(dg);
						return jalaliCurrent[0];
					},
					months: ["فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"],
					daysOfWeek: ["ش", "ی", "د", "س", "چ", "پ", "ج"],
					dayHandler: function (year, monthIndex) {
						var dj = [year, monthIndex, 1];
						var dateJalali = Date.jalaliConverter.jalaliToGregorian(dj);
						var newDate = new Date(dateJalali[0], dateJalali[1] - 1, dateJalali[2]);
						var firstDay;
						var noOfday = newDate.getDay();
						switch (noOfday) {
							case 0:
							case 1:
							case 2:
							case 3:
							case 4:
							case 5:
								firstDay = newDate.getDay() + 1;
								break;
							case 6:
								firstDay = 0;
								break;
						}
						var noInmonth;
						switch (monthIndex) {
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
								noInmonth = 31;
								break;
							case '7':
							case '8':
							case '9':
							case '10':
							case '11':
								noInmonth = 30;
								break;
							case '12':
								noInmonth = 29;
						}
						var reminder = year - Math.floor(year / 33) * 33;
						if (monthIndex == 12) {
							switch (reminder) {
								case 1:
								case 5:
								case 9:
								case 13:
								case 17:
								case 22:
								case 26:
								case 30:
									noInmonth = 30;
							}
						}
						return {firstDayOfWeekIdx: firstDay, noOfDays: noInmonth}
					},
					getTodayHandler: function () {
						var currentDate = new Date();
						var dg = new Array(currentDate.getFullYear(), currentDate.getMonth() + 1, currentDate.getDate());
						var dj = Date.jalaliConverter.gregorianToJalali(dg);
						return {year: dj[0], month: dj[1], day: dj[2]}
					}
				},
				gregorian: {
					direction: "ltr",
					years: function () {
						var currentDate = new Date();
						return currentDate.getFullYear();
					},
					months: ["January", "February", "March", " 	April", "May", "June", "July", "August", "September", "October", "November", "December"],
					daysOfWeek: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
					dayHandler: function (year, monthIndex) {
						var iMonth;
						switch (monthIndex) {
							case "1":
								iMonth = '01';
								break;
							case "2":
								iMonth = '02';
								break;
							case "3":
								iMonth = '03';
								break;
							case "4":
								iMonth = '04';
								break;
							case "5":
								iMonth = '05';
								break;
							case "6":
								iMonth = '06';
								break;
							case "7":
								iMonth = '07';
								break;
							case "8":
								iMonth = '08';
								break;
							case "9":
								iMonth = '09';
								break;
							default :
								iMonth = monthIndex;
						}
						var daysInMonth = 32 - new Date(year, monthIndex - 1, 32).getDate();
						var firstDay = new Date(year + "-" + iMonth + "-01").getDay(); // sunday is 0 and saturday is 6, if monday is 1 and saturday 7 a satatement add after this line : firstday=(firstday===0) ? 7:firstday
						return {firstDayOfWeekIdx: firstDay, noOfDays: daysInMonth}
					},
					getTodayHandler: function () {
						var currentDate = new Date();
						return {
							year: currentDate.getFullYear(),
							month: currentDate.getMonth() + 1,
							day: currentDate.getDate()
						}
					}
				}
			},

			init: function () {
				defaults.divPopup = $("<div></div>");
				defaults.dtTable = $("<table cellpadding='5' cellspacing='0'></table>");
				defaults.dtTHead = $("<thead></thead>");
				defaults.dtTBody = $("<tbody></tbody>");
				defaults.selectYear = $("<select></select>");
				defaults.selectMonth = $("<select></select>");
				defaults.trFirst = $("<tr></tr>");
				defaults.tdFirst = $("<td colspan='7'></td>");

				defaults.yearInput = $("#" + yearId);
				defaults.monthInput = $("#" + monthId);
				defaults.dayInput = $("#" + dayId);

				for (var j = defaults.calendars[configType].years() - 10; j <= defaults.calendars[configType].years() + 10; j++) {
					var txt1 = "<option class='op' value='" + j + "'>" + j + "</option>";
					defaults.selectYear.append(txt1);
				}
				defaults.selectYear.change(defaults.changeYearMonth);

				for (var i = 0; i < defaults.calendars[configType]["months"].length; i++) {
					var txt = "<option value='" + (i + 1) + "'>" + defaults.calendars[configType]["months"][i] + "</option>";
					defaults.selectMonth.append(txt);
				}
				defaults.selectMonth.change(defaults.changeYearMonth);

				defaults.trWeek = $("<tr></tr>");
				for (var k = 0; k < defaults.calendars[configType]["daysOfWeek"].length; k++) {
					var txt2 = "<td>" + defaults.calendars[configType]["daysOfWeek"][k] + "</td>";
					defaults.trWeek.append(txt2);
				}

				if (defaults.yearInput.val() == "")
					defaults.selectYear.val(defaults.calendars[configType].getTodayHandler().year);
				else
					defaults.selectYear.val(defaults.yearInput.val());

				if (defaults.monthInput.val() == "")
					defaults.selectMonth.val(defaults.calendars[configType].getTodayHandler().month);
				else
					defaults.selectMonth.val(defaults.monthInput.val());

				defaults.dayHandlerResult = defaults.calendars[configType].dayHandler(defaults.selectYear.val(), defaults.selectMonth.val());
				defaults.dayOfRow = defaults.dayHandlerResult.noOfDays - (6 - defaults.dayHandlerResult.firstDayOfWeekIdx + 1);
				if (defaults.dayOfRow % 7 == 0) {
					defaults.dayOfRow++;
				}
				defaults.numberOfRow = defaults.dayOfRow / 7;
				if (defaults.numberOfRow > Math.floor(defaults.numberOfRow)) {
					defaults.numberOfRow++;
				}
			},

			renderCalendar: function () {
				defaults.divPopup.append(defaults.dtTable);
				defaults.dtTable.attr("dir", defaults.calendars[configType].direction);
				defaults.dtTable.append(defaults.dtTHead);
				defaults.dtTHead.append(defaults.trFirst);
				defaults.trFirst.append(defaults.tdFirst);
				defaults.tdFirst.append(defaults.selectMonth).append(defaults.selectYear);
				defaults.dtTHead.append(defaults.trWeek);
				var t = 1;
				defaults.dtTable.append(defaults.dtTBody);
				for (var m = 0; m < defaults.numberOfRow; m++) {
					defaults.dtTBody.append("<tr></tr>");
					for (var n = 0; n <= 6; n++) {
						if (n == defaults.dayHandlerResult.firstDayOfWeekIdx) {
							if (t <= defaults.dayHandlerResult.noOfDays) {
								defaults.dtTable.find("tr").last().append("<td style='cursor: pointer'>" + t + "</td>").click(function (e) {
									defaults.dayClick(e);
								});
								defaults.dayHandlerResult.firstDayOfWeekIdx++;
								t++;
							}
						}
						else {
							defaults.dtTable.find("tr").last().append("<td></td>");
						}
					}
					defaults.dayHandlerResult.firstDayOfWeekIdx = 0;
				}
				defaults.dtTable.click(function (e) {
					e.stopPropagation();
				});
			},

			dayClick: function (e) {
				var day = e.target.innerHTML;
				if (day != "") {
					defaults.yearInput.val(defaults.selectYear.val());
					defaults.monthInput.val(defaults.selectMonth.val());
					defaults.dayInput.val(day);
				}
			},

			changeYearMonth: function () {
				defaults.dayHandlerResult = defaults.calendars[configType].dayHandler(defaults.selectYear.val(), defaults.selectMonth.val());
				defaults.dayOfRow = defaults.dayHandlerResult.noOfDays - (6 - defaults.dayHandlerResult.firstDayOfWeekIdx + 1);
				if (defaults.dayOfRow % 7 == 0) {
					defaults.dayOfRow++;
				}
				defaults.numberOfRow = defaults.dayOfRow / 7;
				if (defaults.numberOfRow > Math.floor(defaults.numberOfRow)) {
					defaults.numberOfRow++;
				}
				defaults.dtTBody.empty();
				var t = 1;
				for (var m = 0; m < defaults.numberOfRow; m++) {
					defaults.dtTBody.append("<tr></tr>");
					for (var n = 0; n <= 6; n++) {
						if (n == defaults.dayHandlerResult.firstDayOfWeekIdx) {
							if (t <= defaults.dayHandlerResult.noOfDays) {
								defaults.dtTable.find("tr").last().append("<td>" + t + "</td>").click(function (e) {
									defaults.dayClick(e);
								});
								defaults.dayHandlerResult.firstDayOfWeekIdx++;
								t++;
							}
						}
						else {
							defaults.dtTable.find("tr").last().append("<td></td>");
						}
					}
					defaults.dayHandlerResult.firstDayOfWeekIdx = 0;
				}
			}
		};

		$(this).click(function (e) {
			if ($(defaults.dtTable).length) {
				$(defaults.divPopup).remove();
			}
			defaults.init();
			var parent = $('#' + parentId);
			defaults.divPopup.css({
				position: "absolute",
				zIndex: 1100,
				backgroundColor: "#ffffff",
				border: "1px solid #888888",
				top: parent.position().top + parent.outerHeight(true),
				left: parent.position().left
			});
			defaults.dtTable.addClass("dtPopup");
			$(this).after(defaults.divPopup);
			if (lastOpenDiv == null) {
				lastOpenDiv = defaults.divPopup;
				defaults.renderCalendar();
				e.stopPropagation();
			} else {
				$(lastOpenDiv).remove();
				lastOpenDiv = defaults.divPopup;
				defaults.renderCalendar();
				e.stopPropagation();
			}
		});
	}
})(jQuery);
