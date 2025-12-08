(function($) {
	var PATTERNS = {
		email: /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/,
		url: /^https?:\/\/(.+)\.[a-zA-Z]+$/,
		number: /^[-+]?[0-9]*\.?[0-9]+$/,
		date: /^\d{4}[\/\-]\d{1,2}[\/\-]\d{1,2}$/,
		required: /.+/
	};
	var methods = {
		init: init,
		destroy: destroy,
		validate: validate
	};

	function init() {
		var $elements = this.find('input, select, textarea').filter('[type!=hidden]');
		var form = this;

		// sort the elements by tab index (if available)
		$elements.sort(function (a, b) {
			var aTabIndex = $(a).attr('tabindex');
			var bTabIndex = $(b).attr('tabindex');

			if (aTabIndex === void 0 && bTabIndex === void 0) {
				return 0;
			} else if (aTabIndex === void 0) {
				return 1;
			} else if (bTabIndex === void 0) {
				return -1;
			} else {
				return (aTabIndex - bTabIndex);
			}
		});

		this.data('elements', $elements);

		$elements.on('change.validator', function () {
			validate(this, form);
		});

		return this;
	}

	function destroy() {
		var $elements;
		if ($elements = this.data('elements')) {
			$elements.unbind('.validator');
			this.removeData('elements');
		}

		return this;
	}

	function validate(el, form) {
		var radiosValidated = [];
		var response;
		var i;
		var inputs;

		form || (form = this);

		if (form.data('elWithPopover')) {
			form.data('elWithPopover').popover('destroy');
			form.removeData('elWithPopover');
		}

		inputs = el ? [ el ] : form.data('elements');

		for (i = 0; i < inputs.length && (!response || response.valid); i++) {
			el = inputs[ i ];

			if ((el.type === 'radio' || el.type === 'checkbox') && !radiosValidated[ el.name ]) {
				response = validateRadioCheckbox(el);
				radiosValidated[ el.name ] = response.valid;
			} else {
				response = validateInput(el);
			}

			if (!response.valid) {
				var popoverConfig = {
					content: response.message,
	 				trigger: 'manual',
					template: '<div class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content"><p></p></div></div></div>',
					placement: function (context, source) {
						var position = 'right';
						var targetRightBorder = this.$element.offset().left + this.$element.outerWidth();
						var bodyRightBorder = $('body').offset().left + $('body').outerWidth();
						// var tipRightBorder= targetRightBorder + this.$tip.appendTo('body').outerWidth();
						var tipRightBorder = targetRightBorder + 236; // hardcoded css of fuelux popover;

						this.$tip.remove();

						if (tipRightBorder > bodyRightBorder) {
							position = 'left';
						}

						if (el.type === 'radio' || el.type === 'checkbox') {
							position = 'bottom';
						}

						return position;
					}
				};

				// handle unique date dropdown structure
				if ($(el).parent().is('.date-dropdown-select-boxes')) {
					$(el).parent().css('display', 'inline-block');
					el = $(el).parent()[ 0 ];
				} else {
					el.focus();
				}

				form.data('elWithPopover', $(el));

				if (el.type === 'radio' || el.type === 'checkbox') {
					$(el).parents('.sc-formfield-input-wrapper').siblings('.sc-formfield-label').popover(popoverConfig).popover('show');
				} else {
					$(el).popover(popoverConfig).popover('show');
				}
			} else {
				if (el.type === 'radio' || el.type === 'checkbox') {
					$(el).parents('.sc-formfield-input-wrapper').siblings('.sc-formfield-label').popover('destroy');
				}
			}
		}

		return response ? response.valid : true;

		function validateInput(el, pattern) {
			var $el = $(el);
			var valid = true;
			var isRequired;
			var response;
			var message;
			var confirmationSource;
			var $confirmationSource;
			var MESSAGES = MESSAGES || {
				smartcapture_validation_checkbox: 'Please check this box if you want to proceed.',
				smartcapture_validation_confirm: 'Please confirm.',
				smartcapture_validation_date: 'Please enter a date.',
				smartcapture_validation_email: 'Please enter an email address.',
				smartcapture_validation_number: 'Please enter a number.',
				smartcapture_validation_pattern: 'Please match the requested format.',
				smartcapture_validation_radio: 'Please select one of these options.',
				smartcapture_validation_required: 'Please fill out this field.',
				smartcapture_validation_url: 'Please enter a url.'
			};

			if (confirmationSource = $el.data('confirmation-source')) {
				$confirmationSource = $el.closest('.smartcapture-controls').find(confirmationSource.selector);

				if (pattern = $confirmationSource.val()) {
					pattern = '^' + pattern + '$';

					$el
						.attr('pattern', pattern)
						.attr('required', true)
					;
				} else {
					$el
						.removeAttr('pattern')
						.removeAttr('required')
					;
				}
			}

			// work confirmation field magic before setting these variables
			isRequired = $el.attr('required');
			pattern = $el.attr('pattern') || pattern;

			if (!pattern) {
				if (PATTERNS[ el.type ] && (el.value || isRequired)) {
					response = validateInput(el, PATTERNS[ el.type ]);
					message = MESSAGES[ 'smartcapture_validation_' + el.type ];
				} else if ($el.attr('data-validation')) {
					response = validateInput(el, PATTERNS[ $el.attr('data-validation') ]);
					message = MESSAGES[ 'smartcapture_validation_' + $el.attr('data-validation') ];
				} else if (isRequired) {
					response = validateInput(el, PATTERNS.required);
					message = $el.attr('data-validation-message') || MESSAGES.smartcapture_validation_required;
				}
			} else if (pattern && (el.value || isRequired)) {
				valid = typeof pattern === 'string' ?
					new RegExp(pattern).test(el.value) :
					pattern.test(el.value);

				message = MESSAGES.smartcapture_validation_pattern;
			}

			if (message) {
				// checks for required flag, and, if the no input has been done by user. (gives higher priority to custom validation mesage)
				if(isRequired && !el.value) {
					message = el.title || $el.data('original-title') || $(el).attr('data-validation-message') || message;
				}
				// Gives higher priority to inpput validation messages if above check returns false.
				else {
					message = el.title || $el.data('original-title') || MESSAGES[ $el.attr('data-validation') ] || $(el).attr('data-validation-message') || message;
				}
			}

			if (response) {
				if (message) {
					response.message = message;
				}
			} else {
				response = {
					valid: valid,
					message: message
				};
			}

			return response;
		}

		function validateRadioCheckbox(el) {
			var elements;
			var isRequired = false;
			var isChecked = false;
			var message = '';
			var valid;
			var type = el.type;
				if (type === 'radio'){
					elements = $('[type="radio"][name="' + el.name + '"]');
				}	else {
					elements = $('[type="checkbox"][name="' + el.name + '"]');
				}

			// look at each item in the group
			// if at least one of them contain a 'required' attribute then validate
				elements.each(function (index, element) {
					if (!isRequired && $(this).attr('required')) {
						isRequired = true;
					}

					if (!isChecked && this.checked) {
						isChecked = true;
					}

					if (!isRequired || isChecked) {
						valid = true;
						return false;
					}
				});

			// allow title override for all messages
			if (!valid) {
				message = elements.attr('title') || elements.data('original-title') || $(el).attr('data-validation-message') || (type === 'radio' ? MESSAGES.radio : MESSAGES.checkbox);
			}

			return {
				valid: valid,
				message: message
			};
		}
	}

	$.fn.validator = function (method) {
		if (methods[ method ]) {
			return methods[ method ].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		}
	};
})(jQuery);
