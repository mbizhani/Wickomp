package org.devocative.wickomp.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.ValidationError;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;

public class WCaptchaInput extends WLabeledFormInputPanel<String> {
	private static final long serialVersionUID = -909490839684383578L;

	private String randomText;
	private Image image;
	private CaptchaImageResource captchaImageResource;
	private TextField<String> text;

	private int minLength = 4;
	private int maxLength = 6;
	private TextMode textMode = TextMode.Number;

	// ------------------------------

	public WCaptchaInput(String id) {
		super(id, new Model<>());
	}

	// ------------------------------

	public WCaptchaInput setMinLength(int minLength) {
		this.minLength = minLength;
		return this;
	}

	public WCaptchaInput setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	public WCaptchaInput setTextMode(TextMode textMode) {
		this.textMode = textMode;
		return this;
	}

	// ---------------

	@Override
	public void convertInput() {
		setConvertedInput(text.getConvertedInput());
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setRequired(true);

		captchaImageResource = createCaptchaImageResource();
		image = new NonCachingImage("image", captchaImageResource);
		image.setOutputMarkupId(true);
		add(image);

		add(new WAjaxLink("refresh") {
			private static final long serialVersionUID = 6898606396493989429L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				captchaImageResource.invalidate();
				text.setModel(new Model<>());

				target.add(image);
				target.add(text);
			}
		});

		text = new TextField<String>("text", new Model<>(), String.class) {
			private static final long serialVersionUID = -5994624882748775311L;

			protected final void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("value", "");
			}
		};
		text.setOutputMarkupId(true);
		add(text);

		add(validatable -> {
			String value = validatable.getValue();
			if (!randomText.equalsIgnoreCase(value)) {
				ValidationError error = new ValidationError();
				error.addKey("InvalidCaptcha");
				error(error);
			}
		});

		add(new FontAwesomeBehavior());
	}

	@Override
	protected void onBeforeRender() {
		captchaImageResource.invalidate();
		super.onBeforeRender();
	}

	// ------------------------------

	private CaptchaImageResource createCaptchaImageResource() {
		return new CaptchaImageResource() {
			private static final long serialVersionUID = -1067598536499885118L;

			@Override
			protected byte[] render() {
				randomText = randomString(minLength, maxLength);
				getChallengeIdModel().setObject(randomText);
				return super.render();
			}
		};
	}

	private int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	private String randomString(int min, int max) {
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			switch (textMode) {
				case a2z:
					b[i] = (byte) randomInt('a', 'z');
					break;
				case A2Z:
					b[i] = (byte) randomInt('A', 'Z');
					break;
				case Number:
					b[i] = (byte) randomInt('0', '9');
					break;
				case Mixed:
					int rand = (int) (Math.random() * 10);
					switch (rand % 3) {
						case 0:
							b[i] = (byte) randomInt('a', 'z');
							break;
						case 2:
							b[i] = (byte) randomInt('A', 'Z');
							break;
						default:
							b[i] = (byte) randomInt('0', '9');
					}
					break;
			}
		return new String(b);
	}

	// ------------------------------

	public enum TextMode {a2z, A2Z, Number, Mixed}
}
