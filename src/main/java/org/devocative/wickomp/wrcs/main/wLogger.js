var wLog = {
	Levels: {
		Debug: 'debug',
		Info: 'info',
		Warn: 'warn',
		Error: 'error',
		None: 'none'
	},
	_curLevel: 10,
	_orderOfLevels: ['debug', 'info', 'warn', 'error', 'none'],

	setLevel: function (lvl) {
		wLog._curLevel = wLog._orderOfLevels.indexOf(lvl);
		console.log("******* WLog Level:", lvl, wLog._curLevel, "*******");
	},

	debug: function () {
		wLog._log(wLog.Levels.Debug, '[DEBUG]', arguments);
	},

	info: function () {
		wLog._log(wLog.Levels.Info, '[INFO] ', arguments);
	},

	warn: function () {
		wLog._log(wLog.Levels.Warn, '[WARN] ', arguments);
	},

	error: function () {
		wLog._log(wLog.Levels.Error, '[ERROR]', arguments);
	},

	_log: function (level, levelTitle, args) {
		var idx = wLog._orderOfLevels.indexOf(level);
		if (console && idx >= wLog._curLevel) {
			var argArr = Array.prototype.slice.call(args);
			argArr.unshift(levelTitle);

			var now = new Date();
			var str = '(' + wLog._pad(now.getHours()) + ':' + wLog._pad(now.getMinutes()) + ':' + wLog._pad(now.getSeconds()) + ')';
			argArr.unshift(str);

			switch (level) {
				case wLog.Levels.Debug:
					console.debug.apply(console, argArr);
					break;
				case wLog.Levels.Info:
					console.info.apply(console, argArr);
					break;
				case wLog.Levels.Warn:
					console.warn.apply(console, argArr);
					break;
				case wLog.Levels.Error:
					console.error.apply(console, argArr);
					break;
			}
		}
	},

	_pad: function (num) {
		return num < 10 ? '0' + num : num;
	}
};

wLog.setLevel(wLog.Levels.Debug);
