(function (root, factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define(['jquery'], function ($) {
			return factory(root, root.Fuel || {}, $);
		});
	} else if (typeof exports === 'object') {
		// Node. Does not work with strict CommonJS, but
		// only CommonJS-like environments that support module.exports,
		// like Node.
		module.exports = factory(root, root.Fuel || {}, require('jquery'));
	} else {
		// Browser globals
		root.Fuel = factory(root, root.Fuel || {}, root.jQuery || root.$);
	}
}(this, function (window, Fuel, $) {
	var isNU = function (item) { return item === null || undefined === item; };

	Fuel.TrackingEventTypes = { Click: 'click', View: 'view' };

	function trackingContext (clientID, enterpriseID) {
		if (isNU(clientID) || parseInt(clientID, 10) < 1) {
			throw { name: 'Context Error', message: 'Invalid ClientID' };
		}
		if (isNU(enterpriseID) || parseInt(enterpriseID, 10) < 1) {
			throw { name: 'Context Error', message: 'Invalid EnterpriseID' };
		}

		var _clientID = parseInt(clientID, 10);
		var _enterpriseID = parseInt(enterpriseID, 10);

		this.EnterpriseID = _enterpriseID;
		this.ClientID = _clientID;
	}

	Fuel.TrackingContext = trackingContext;

	function tracking (trackingContext, defaultBag, options) {
		return {
			wrapLinks: function (classNames, trackedEventType, extraProps) {
			},

			addRawEvent: function (trackedEventType, bagItems) {
			},

			send: function () {
			},

			bindDomEvent: function (element, domEventName, trackedEventType, extraProps) {
			},

			unbindDomEvent: function (element, domEventName, trackedEventType) {
			},

			bindClickEvent: function (element, trackedEventType, extraProps) {
			},

			unbindClickEvent: function (element, trackedEventType) {
			}
		};
	}

	Fuel.Tracking = tracking;

	return Fuel;
}));
