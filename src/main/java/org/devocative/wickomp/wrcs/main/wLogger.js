var wLog = {
	Levels: {
		Debug: 'debug',
		Info: 'info',
		Warn: 'warn',
		Error: 'error',
		None: 'none'
	},
	_level: '',
	_debug: ['debug'],
	_info: ['info', 'debug'],
	_warn: ['warn', 'info', 'debug'],
	_error: ['error', 'warn', 'info', 'debug'],

	setLevel: function (lvl) {
		wLog._level = lvl;
	},

	debug: function () {
		wLog._log(wLog.Levels.Debug, '[DEBUG]', wLog._debug, arguments);
	},

	info: function () {
		wLog._log(wLog.Levels.Info, '[INFO] ', wLog._info, arguments);
	},

	warn: function () {
		wLog._log(wLog.Levels.Warn, '[WARN] ', wLog._warn, arguments);
	},

	error: function () {
		wLog._log(wLog.Levels.Error, '[ERROR]', wLog._error, arguments);
	},

	_log: function (level, levelTitle, levelArr, args) {
		if (console && levelArr.indexOf(wLog._level) > -1) {
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
