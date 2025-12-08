(function (root, factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define([ 'jquery' ], function ($) {
			return factory(root, $);
		});
	} else if (typeof exports === 'object') {
		// Node. Does not work with strict CommonJS, but
		// only CommonJS-like environments that support module.exports,
		// like Node.
		module.exports = factory(root, require('jquery'));
	} else {
		// Browser globals
		root.ScForm = factory(root, root.jQuery || root.$);
	}
}(this, function (window, $) {

	var getAppDomainUri = function () {
		return window.appDomain ? ('//' + window.appDomain) : '';
	};
	var getBaseUrl = function () {
		var location = window.location;
		var pathName = location.pathname.split('/')[ 1 ];
		return location.protocol + '//' + location.host + '/' + pathName;
	};
	var getCheckResponse = function (success, error) {
		return function (resp) {
			if (resp === true) {
				success && success();
			} else {
				error && error();
			}
		};
	};
	var langs = ['de', 'en', 'es-xl', 'es', 'fr-ca', 'fr', 'it', 'ja', 'pt'];
	var LibUtils = {
		createTag: function (tag, id, attributes) {
			var el;
			var prop;
			var document = window.document;
			var head = document.getElementsByTagName('head')[ 0 ];
			var tagEl = document.getElementById(id);

			if (!tagEl) {
				el = document.createElement(tag);
				el.id = id;

				for (prop in attributes) {
					if (attributes.hasOwnProperty(prop)) {
						el[ prop ] = attributes[ prop ];
					}
				}
				head.appendChild(el);
			} else if (attributes && attributes.onload) {
				if (tagEl.addEventListener) {                    // For all major browsers, except IE 8 and earlier
					tagEl.addEventListener('load', attributes.onload);
				} else if (tagEl.attachEvent) {                  // For IE 8 and earlier versions
					tagEl.attachEvent('onload', attributes.onload);
				}
			}
		},
		createLink: function (id, href, onload) {
			LibUtils.createTag('link', id, { rel: 'stylesheet', href: href, onload: onload });
		},
		createScript: function (id, src, onload, async) {
			LibUtils.createTag('script', id, { src: src, onload: onload, async: !!async });
		},
		getItem: function (itemCheck, id, src, onload, async) {
			if (itemCheck) {
				LibUtils.createScript(id, src, onload, async);
			} else {
				onload();
			}
		},
		getAsyncItem: function (itemCheck, id, src, async) {
			var defer = $.Deferred();

			LibUtils.getItem(itemCheck, id, src, function () {
				defer.resolve();
			}, async);

			return defer.promise();
		},
		load: {
			jQuery: function (cb) {
				LibUtils.getItem((!($ || window.jQuery || window.$)), 'jquery-script', '//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js', function () {
					if (!$) {
						$ = window.jQuery || window.$;
					}
					cb && cb();
				});
			},
			FuelTracking: function () {
				return LibUtils.getAsyncItem((!window.Fuel || !window.Fuel.Tracking || !window.Fuel.TrackingContext), 'fuel-tracking-script', getAppDomainUri() + '/CloudPages/utilities/t.js', true);
			},
			jQueryValidatorMessages: function () {
				const navigatorLanguage = navigator.language.toLowerCase();
				const lang = langs.indexOf(navigatorLanguage) >= 0 ? navigatorLanguage : 'en';
				return LibUtils.getAsyncItem(true, 'jquery-validator-messages', getAppDomainUri() + '/CloudPages/lib/sc-validation-messages/' + lang + '.js', true);
			},
			jQueryValidator: function () {
				return LibUtils.getAsyncItem((!$.fn.validator), 'jquery-validator-script', getAppDomainUri() + '/CloudPages/lib/jquery.validator.js', true);
			},
			BootstrapDatepicker: function () {
				var datepickerLoaded = (!$.fn.datepicker || !$.fn.datepicker.DPGlobal);

				LibUtils.getAsyncItem(datepickerLoaded, 'jquery-ui-script', '//ajax.googleapis.com/ajax/libs/jqueryui/1.8.22/jquery-ui.min.js');
				LibUtils.getAsyncItem(datepickerLoaded, 'fuelux-loader-script', '//www.fuelcdn.com/fuelux/2.3/loader.min.js');

				return LibUtils.getAsyncItem(datepickerLoaded, 'bootstrap-datepicker-script', getAppDomainUri() + '/CloudPages/lib/bootstrap-datepicker.js', true);
			}
		}
	};

	/**
	 * Toggles the state of the submit button to disabled double submitting
	 * @param {jquery element} button
	 * @param {boolean} disabled
	 */
	function toggleButtonState (button, disabled) {
		if (disabled) {
			button.attr('disabled', 'disabled');
			button.attr('style', "cursor: not-allowed; opacity: .1");
		} else {
			button.removeAttr('disabled');
			button.removeAttr('style');
		}
	}

	var ScForm;

	ScForm = function (options) {
		var self = this;
		var contentDetail = window.contentDetail || {};
		var channelOverrides = window.channelOverrides || {};

		// contentDetail provided by f.html on window scope
		this._useJourneyBuilder = !!contentDetail.triggerJourneyBuilderEvent;
		this._gearID = options.gearID;
		this._smartCaptureFormID = options.smartCaptureFormID;
		this._sourceKey = options.sourceKey;
		this._source = options.source;
		this._triggeredSend = options.triggeredSend;
		this._confirmationMessage = options.confirmationMessage;
		this.options = options;
		this.$smartCapture = $('#smartcapture-block-' + this._gearID);

		this.$smartCapture
			.on('submit', submitClicked)
			.on('click', '.alert button', function (e) {
				if (e) {
					if (e.preventDefault) e.preventDefault();
					if (e.stopPropagation) e.stopPropagation();
				}
				self.$smartCapture.removeClass('alerting');
			})
			// assign value to isMobile field since it is unavailable from SSJS in form.html
			.find('input[name="IsMobile"]')
			.val(contentDetail.isMobile)
		;

		initializeDatePickers();
		initializeDateDropdown();
		initializeCharacterLimits();
		initializeTabIndexes();

		cleanPersonalizationValues();

		injectPersonalizationValues();

		modalZIndexFix();

		// turn on validation
		this.$smartCapture.validator();

		function modalZIndexFix() {
			// move all modals to the top to prevent z-index issues
			self.$smartCapture
				.find('.fuelux > .modal')
				.parent()
				.appendTo('body')
			;
		}

		function initializeDateDropdown() {
			var dayMap = {
				'1': 31,
				'2': 28,
				'3': 31,
				'4': 30,
				'5': 31,
				'6': 30,
				'7': 31,
				'8': 31,
				'9': 30,
				'10': 31,
				'11': 30,
				'12': 31
			};

			self.$smartCapture
				.find('.date-dropdown-select-boxes')
				.on('change', '.select', _updateInput)
			;

			function _updateInput(e) {
				var $parentEl = $(e.currentTarget).parent('.date-dropdown-select-boxes');
				var $el = $(e.currentTarget);
				var $dropdowns = $parentEl.find('.select');
				var $dayDropdown = $dropdowns.filter('.day');
				var aPattern = $parentEl.data('pattern').split(' ');
				var aValue = [];
				var sValue = '';
				var sSelection = '';
				var iDayIndex = 31;
				var oSelections = {
					'day': void 0,
					'month': void 0,
					'year': void 0
				};

				$.each(aPattern, function (index, item) {
					sSelection = $dropdowns.filter('.' + item).val();

					if (sSelection !== '') {
						oSelections[ item ] = sSelection * 1;
						aValue.push(sSelection);
					} else {
						oSelections[ item ] = void 0;
					}
				});

				if ($el.is('.month, .year')) {
					iDayIndex = dayMap[ oSelections.month ];

					// february leap year
					if (oSelections.year % 4 === 0 && oSelections.month === 2) {
						iDayIndex += 1;
					}

					$dayDropdown
						.find('option')
						.show()
						.filter(':gt(' + iDayIndex + ')')
						.hide();

					// if selected day is now hidden clear selected day
					if (oSelections.day > iDayIndex) {
						$dayDropdown.prop('selectedIndex', 0);
						aValue = [];
					}
				}

				if (aValue.length === 3) {
					sValue = aValue.join('/');
				}

				$parentEl.find('input').val(sValue);
			}
		}

		function initializeDatePickers() {
			self.$smartCapture
				.find('.activefield-datepicker')
				.each(activateDatePicker)
			;

			function activateDatePicker() {
				var $this = $(this);
				var format = $this.attr('data-date-format') || 'mm/dd/yyyy';

				$this.datepicker({
					'format': format,
					'appendTo': 'parent'
				});
			}
		}

		function initializeCharacterLimits() {
			self.$smartCapture
				.find('.characterLimit')
				.on('keyup', setAvailable)
				.on('keydown', 'textarea, input', checkLength)
				.keyup()
			;

			function setAvailable(e) {
				var $control = $(e.currentTarget);
				var $helpBlock = $control.find('.help-block');
				var $input = $control.find('textarea, input');
				var $charCount = $helpBlock.find('.basic-length-count');
				var maxLength = $input.attr('maxlength');
				var length = $input.val().length;

				$charCount.text(maxLength - length);

				$helpBlock.toggleClass('text-warning', length > (maxLength * 0.9));
			}

			function checkLength(e) {
				var input = e.currentTarget;

				return !!input.maxLength
					|| $(input).val().length < maxLength
					|| (e.which === 8 || e.which === 46 || (e.which >= 35 && e.which <= 40));
			}
		}

		function initializeTabIndexes() {
			var elementTypesToInclude = 'input, select, textarea, button';
			var $scInstances = $('.smartcapture-content-wrapper');
			var totalElements = $scInstances.find(elementTypesToInclude).filter(':visible').length;
			var elements = self.$smartCapture.find(elementTypesToInclude).filter(':visible');
			// figure out this form's position relative to others
			var index = $scInstances.index(self.$smartCapture);

			// pad for other instances, defaulting to 1
			// padding is based on the total elements on all forms
			// this will safely provide enough tab index padding between forms
			if (index < 1) {
				index = 1
			} else {
				index *= totalElements;
			}

			// sort elements from top to bottom, left to right
			elements.sort(function (x, y) {
				var xPos = $(x).offset();
				var yPos = $(y).offset();
				var diff = xPos.top - yPos.top;

				return diff !== 0 ? diff : xPos.left - yPos.left;
			});

			elements.each(function () {
				this.tabindex = index++;
			});
		}

		function submitClicked(e) {
			var $smartCapture = self.$smartCapture;
			toggleButtonState($smartCapture.find('.sc-button'), true);

			if (e && e.preventDefault) e.preventDefault();

			if (!$smartCapture.validator('validate')) {
				toggleButtonState($smartCapture.find('.sc-button'), false);
				return false;
			}

			$smartCapture.addClass('submitting');

			send();

			// prevent form from submitting
			return false;
		}

		function getInputs() {
			return $(self.$smartCapture.get(0).elements)
				.filter('[name]')
			;
		}

		function cleanPersonalizationValues() {
			var purlRe = /%{2}.+%{2}/;

			getInputs().each(function () {
				if (purlRe.test(this.value)) {
					this.value = '';
				}
			});
		}

		function injectPersonalizationValues() {
			self.$smartCapture
				.find('[data-value]')
				.each(function () {
					var $this = $(this);
					var value = $this.attr('data-value');
					var type = $this.attr('data-control-type');

					if (value && type) {
						chooseInjectorByControlType(type, $this, value);
					}
				});
		}

		function chooseInjectorByControlType(type, $control, value) {
			switch (type) {
				case 'select':
					$control.val(value);
					break;
				case 'date':
					$control.datepicker('update', value);
					break;
				case 'combo':
					$control.combobox('selectByText', value);
					break;
			}
		}

		function send() {
			var attributes = [];
			var _attributes = {};
			var postURL = getBaseUrl() + '/smartcapture/post';
			var payload;
			var emailAddresses = [];

			// TODO: move to class such as '.smartcapture-submission-value' instead of tags (sww 20130719)
			getInputs().each(function (index, el) {
					var $this = $(this);
					var name = el.name;
					var label = name;
					var value = el.value || '';
					var type = el.type;
					var checked = $this.prop('checked');
					var currentValue;

					// only include radio button if it is selected
					if (type === 'radio' && !checked) return;

					// transform checkbox values into 1/0 or true/false based on fieldtype
					if (type === 'checkbox') {
						if ($this.attr('data-field-type') === 'Boolean') {
							value = checked;
						} else if (!$this.attr('value')) {
							value = checked ? '1' : '0';
						} else {
							currentValue = _attributes[ label ] || '';
							if (checked) {
								currentValue += (currentValue ? ',' : '') + value;
							}

							value = currentValue;
						}
					}

					// handle date picker
					if ($this.parent().is('.date')) {
						value = $this
							.parent()
							.data('datepicker')
							.getFormattedDate(
								// data extension prefers format mm/dd/yyyy so regardless of selected/displayed format save in this format
								{
									parts: [ 'mm', 'dd', 'yyyy' ],
									separators: [ '', '/', '/', '' ]
								}
							);
					}

					// handle confirmation input
					if ($this.data('confirmation-source')) return;

					_attributes[label] = String(value);

                if (type === "email") {
                        // using array since there can be more than one EmailAddress on a form
                        emailAddresses.push('"' + label + '":"' + value + '"');
					}
				})
			;

			$.each(_attributes, function (key, value) {
				attributes.push('"' + key + '":"' + encodeURIComponent(value) + '"');
			});

			payload = {
                emailAddress: '{' + emailAddresses.join(',') + '}',
				formID: self._smartCaptureFormID,
				targetID: self._sourceKey,
				targetType: self._source,
				attributes: '{' + attributes.join(',') + '}',
				withTriggeredSend: self._triggeredSend,
				isJourneyBuilderIntegrated: self._useJourneyBuilder
			};

			/**
			 * Allows a channel to override this URL when needed.
			 * @see /cp-channels/facebook-tab/data/default.html for an example.
			 *
			 * NOTE: by default, Landing Pages and Mobile Push channels use the default value for postURL.
			 */
			if (channelOverrides.smartCapturePostURL) {
				postURL = channelOverrides.smartCapturePostURL;
			}

			$.ajax({
				url: postURL,
				type: 'post',
				dataType: 'json',
				data: payload,
				success: getCheckResponse(success, error),
				error: error
			});

			sendTrackingData();

			function success() {
				var _options = self.options;
				var goToUrl;

				if (_options.onSubmitShouldGotoUrl) {
					goToUrl = _options.onSubmitGotoUrl;

					if (_options.onSubmitGotoUrlType === 2) {
						window.top.location.assign(goToUrl);
					} else {
						displayConfirmationMessage();
						window.open(goToUrl);
					}
				} else {
					displayConfirmationMessage();
				}

				self.$smartCapture.removeClass('submitting');
				sendComplete();

				function displayConfirmationMessage() {
					self.$smartCapture.html(self._confirmationMessage);
				}
			}

			function error() {
				self.$smartCapture
					.removeClass('submitting')
					.addClass('alerting')
				;

				toggleButtonState(self.$smartCapture.find('.sc-button'), false);

				sendComplete();
			}

			function sendComplete() {
				// scroll to top of form and hard left
				window.scrollTo(0, self.$smartCapture.offset().top);
			}

			function sendTrackingData() {
				var Fuel = window.Fuel;
				var scT = new Fuel.Tracking(
					new Fuel.TrackingContext(contentDetail.mid, contentDetail.eid),
					contentDetail.isSite ?
					{
						siteid: String(contentDetail.siteID),
						pageid: String(contentDetail.pageID),
						isMobile: String(contentDetail.isMobile)
					} :
					{
						isFan: String(contentDetail.liked),
						tabid: String(contentDetail.fbTabID),
						pageid: String(contentDetail.fbPageID),
						tabcontentid: String(contentDetail.fbTabContentID),
						contentid: String(contentDetail.contentID),
						appid: String(contentDetail.facebookAppID),
						triggerJourneyBuilderEvent: String(contentDetail.triggerJourneyBuilderEvent),
						isMobile: String(contentDetail.isMobile)
					}
				);

				scT.addRawEvent('CLOUDPAGESGEARINTERACTION', {
					'gearid': self._gearID,
					'gearType': '2157FF01-C76C-499F-A44F-E1F33825DD5C'
				});
				scT.send();
			}
		}
	};

	return {
		init: function (options) {
			// LibUtils.createLink('fuelux-stylesheet', getAppDomainUri() + '/CloudPages/css/fuelux.css');
			LibUtils.createLink('datepicker-stylesheet', getAppDomainUri() + '/CloudPages/css/datepicker.css');
			LibUtils.createLink('smartcapture-stylesheet', getAppDomainUri() + '/CloudPages/css/smartcapture-form.css');

			LibUtils.load.jQuery(function () {
				$.when(
					LibUtils.load.FuelTracking(),
					LibUtils.load.BootstrapDatepicker(),
					LibUtils.load.jQueryValidatorMessages(),
					LibUtils.load.jQueryValidator()
				).then(function () {
					new ScForm(options);
				});
			});
		}
	};
}));
