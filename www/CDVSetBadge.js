var exec = require('cordova/exec');

exports.setBadge = function (arg0, success, error) {
    exec(success, error, 'CDVSetBadge', 'setBadge', [arg0]);
};