package org.devocative.wickomp.demo.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.devocative.adroit.vo.KeyValueVO;
import org.devocative.adroit.vo.RangeVO;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.panel.SelectionPanel;
import org.devocative.wickomp.demo.vo.Field;
import org.devocative.wickomp.demo.vo.KeyValue;
import org.devocative.wickomp.demo.vo.PersonVO;
import org.devocative.wickomp.form.*;
import org.devocative.wickomp.form.code.OCode;
import org.devocative.wickomp.form.code.OCodeMode;
import org.devocative.wickomp.form.code.WCodeInput;
import org.devocative.wickomp.form.range.WDateRangeInput;
import org.devocative.wickomp.form.range.WTextRangeInput;
import org.devocative.wickomp.form.validator.WPasswordStrengthValidator;
import org.devocative.wickomp.html.WEasyLayout;
import org.devocative.wickomp.html.WFloatTable;
import org.devocative.wickomp.html.WMessager;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class FormPage extends BasePage {
	private static final long serialVersionUID = 5898646496431514878L;

	private WEasyLayout layout;
	private WebMarkupContainer west;

	public FormPage() {
		west = new WebMarkupContainer("west");
		layout = new WEasyLayout("layout");
		layout.setWestOfLTRDir(west);
		layout.add(west);
		add(layout);

		simpleForm();
		dynamicForm();
		theCaptcha();

		add(new EasyUIBehavior());
	}

	private void dynamicForm() {
		List<Field> fields = new ArrayList<>();
		fields.add(new Field("RO", "readOnly", Field.Type.RO));
		fields.add(new Field("Name", "name", Field.Type.SQL));
		fields.add(new Field("Age", "age", Field.Type.Integer));
		fields.add(new Field("Weight", "weight", Field.Type.Real));
		fields.add(new Field("Date", "date", Field.Type.Date));
		fields.add(new Field("Alive", "alive", Field.Type.Boolean));

		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe%");
		map.put("age", 123456);
		map.put("readOnly", 987654321);

		WFloatTable floatTable = new WFloatTable("floatTable");
		floatTable.add(new ListView<Field>("fields", fields) {
			private static final long serialVersionUID = -6710943314522675829L;

			@Override
			protected void populateItem(ListItem<Field> item) {
				Field field = item.getModelObject();
				//item.add(new TextField<String>("field"));

				RepeatingView view = new RepeatingView("field");
				FormComponent fc = null;
				switch (field.getType()) {

					case RO:
						fc = new WLabelInput(field.getName())
							.setRequired(true);
						break;

					case String:
						fc = new WTextInput(field.getName())
							.setRequired(true);
						break;

					case SQL:
						fc = new WSqlStringInput(field.getName())
							.setRequired(true);
						break;

					case Integer:
						fc = new WNumberInput(field.getName(), Long.class)
							.removeThousandSeparator()
							.setRequired(true);
						break;

					case Real:
						fc = new WNumberInput(field.getName(), BigDecimal.class)
							.setPrecision(4)
							.setThousandSeparator(',');
						break;

					case Boolean:
						fc = new WBooleanInput(field.getName())
							.setRequired(true);
						break;

					case Date:
						fc = new WDateInput(field.getName())
							.setTimePartVisible(true)
							.setDefaultTime(12, 0, 0, 0)
							.setRequired(true);
				}

				fc.setLabel(new Model<>(field.getTitle()));
				view.add(fc);

				item.add(view);
			}
		});
		floatTable.add(new WTextInput("text1").setLabel(new Model<>("Txt1")).setVisible(false));
		floatTable.add(new WTextInput("text2").setLabel(new Model<>("Txt2")));

		Form<Map<String, Serializable>> dynamicForm = new Form<>("dynamicForm", new CompoundPropertyModel<>(map));
		dynamicForm.add(floatTable);
		//dynamicForm.add(new Button("save") {
		dynamicForm.add(new WAjaxButton("save") {
			private static final long serialVersionUID = -7991701558774150634L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println(map);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
				WMessager.show(getString("label.error", null, "Error"), errors, target);
			}

			/*@Override
			public void onSubmit() {
				System.out.println(map);
			}*/
		});
		layout.add(dynamicForm);
	}

	private String sql;
	private String groovy;

	final Map<String, Serializable> map = new HashMap<>();

	private void simpleForm() {
		List<KeyValue> list = new ArrayList<>();
		list.add(new KeyValue("A", "Alef"));
		list.add(new KeyValue("B", "Be"));
		list.add(new KeyValue("C", "Cee"));
		list.add(new KeyValue("D", "Dee"));

		OCode oCode = new OCode(OCodeMode.SQL);
		Map<String, Map<String, String>> tables_cols = new HashMap<>();
		for (int i = 1; i < 9000; i++) {
			String table = String.format("t_tbl%06d", i);
			Map<String, String> cols = new HashMap<>();
			for (int c = 1; c < ((int) (Math.random() * 100)) + 5; c++) {
				String col = String.format("c_%05d_%02d", i, c);
				cols.put(col, null);
			}
			tables_cols.put(table, cols);
		}
		Map<String, Map> tables = new HashMap<>();
		tables.put("tables", tables_cols);
		oCode
			.setHintOptions(tables)
		//.setShowMatchingBrackets(false)
		;


		List<PersonVO> personVOs = PersonVO.list();

		final WSelectionInput child, parentSI;
		map.put("name", "Joe!@#$%^&*()_+-=[]{}\\|/?.,><");
		map.put("lblIn", new KeyValue("KV-LBL-IN", "KV-LBL-IN"));
		map.put("eduSingle", new KeyValue("A"));
		map.put("eduMultiple", (Serializable) Arrays.asList(new KeyValue("A"), new KeyValue("D")));
		map.put("child", "B.1");
		map.put("dateRange", new RangeVO<>(null, new Date()));
		map.put("age", 2000);
		map.put("orderedPerson", new ArrayList<>(personVOs.subList(4, 8)));

		// Multiple
		map.put("kvList", (Serializable) Arrays.asList(new KeyValueVO<>("A", "A"), new KeyValueVO<>("B", "B")));
		// Single
//		map.put("kvList", new KeyValueVO<>("A", "A"));

		Form<Map<String, Serializable>> form = new Form<>("form", new CompoundPropertyModel<>(map));

		form.add(new WLabelInput("lblIn"));
		form.add(new WTextInput("name")
			.setSize(30)
			.setRequired(true)
			.add(new AttributeModifier("size", "50"))
		);
//		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$")));
//		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$", "name.format")));
		form.add(new WTextInput("password", true).add(new WPasswordStrengthValidator()));
//		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$").setCustomMessage("oops!")));

		//form.add(new WNumberRangeInput("age", Integer.class).setThousandSeparator(","));
		form.add(new WNumberInput("age", Integer.class).setThousandSeparator(','));
		form.add(new WSelectionInput("eduSingle", list, false));
		form.add(new WSelectionInput("eduMultiple", list, true));
		form.add(new DropDownChoice<>("eduDD", list));
		form.add(new WDateInput("birthdate"));
		form.add(new WBooleanInput("alive"));
		form.add(new WDateRangeInput("dateRange").setTimePartVisible(true));
		form.add(new WTextRangeInput("stringRange").setSize(40).setMaxlength(5));
		form.add(parentSI = new WSelectionInput("parent", Arrays.asList("A", "B", "C"), false));
		form.add(child = new WSelectionInput("child", Arrays.asList("B.1"), false));
		form.add(new WCodeInput("sql", new PropertyModel<>(this, "sql"), oCode));
		form.add(new WCodeInput("groovy", new PropertyModel<>(this, "groovy"), new OCode(OCodeMode.GROOVY)));
		form.add(new WClientSearchableListInput("kvList", true) {
			private static final long serialVersionUID = 4542301240930720140L;

			{
				getModalWindowOptions().setWidth(OSize.percent(80));
			}

			@Override
			protected Component createSelectionPanel(String selectionPanelId) {
				SelectionPanel p = new SelectionPanel(selectionPanelId);
				p.setJS(getJSCallback());
				return p;
			}

			@Override
			protected KeyValueVO<String, String> createServerObject(String key) {
				return new KeyValueVO<>(key, null);
			}

			@Override
			protected List<KeyValueVO<String, String>> createClientOptions(List list) {
				return list;
			}
		}.setOpenModalLinkVisible(true));
		form.add(new WOrderedListInput<>("orderedPerson", personVOs).setVisibleSize(10));

		/*form.add(new Button("save") {
			private static final long serialVersionUID = -8861063203041673554L;

			public void onSubmit() {
				theSubmit();
			}

			@Override
			protected void onAfterRender() {
				super.onAfterRender();
				FeedbackMessagesModel feedbackMessagesModel = new FeedbackMessagesModel(this);
				List<FeedbackMessage> messages = feedbackMessagesModel.getObject();
				List<Serializable> errors = new ArrayList<>();
				if (messages.size() > 0) {
					for (FeedbackMessage message : messages) {
						errors.add(message.getMessage());
					}
					String st = WMessager.getScript("Err", WMessager.getHtml(errors));
					getWebResponse().write(String.format("<script>$(function(){%s});</script>", st));
				}
			}

		});*/

		form.add(new WAjaxButton("save") {
			private static final long serialVersionUID = -5778189211832263083L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				theSubmit();
			}

			@Override
			protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
				WMessager.show(getString("label.error", null, "Error"), errors, target);
			}
		});

		west.add(form);

		parentSI.addToChoices(new WSelectionInputAjaxUpdatingBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Object parentSel = getComponent().getDefaultModelObject();
				child.updateChoices(target, Arrays.asList(
					String.format("%s.1", parentSel),
					String.format("%s.2", parentSel),
					String.format("%s.3", parentSel)
				));
			}
		});
	}

	private void theSubmit() {
		System.out.println("MAP {{");
		for (Map.Entry<String, Serializable> entry : map.entrySet()) {
			System.out.printf("\t%s = %s (%s)\n", entry.getKey(), entry.getValue(),
				entry.getValue() != null ? entry.getValue().getClass().getName() : "-");
		}
		System.out.println("}}");

		System.out.println("sql = " + sql);

		System.out.println("groovy = " + groovy);
	}

	private void theCaptcha() {
		Form<Void> form = new Form<>("captchaForm");
		form.add(new WCaptchaInput("captcha"));
		form.add(new WAjaxButton("submit") {
			private static final long serialVersionUID = -8661601583848726777L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				WMessager.show("!", "ok!", target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
				WMessager.show("!", errors, target);
			}
		});
		layout.add(form);
	}
}
