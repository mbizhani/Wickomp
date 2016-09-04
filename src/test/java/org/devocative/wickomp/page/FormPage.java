package org.devocative.wickomp.page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
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
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.form.*;
import org.devocative.wickomp.form.code.OCode;
import org.devocative.wickomp.form.code.OCodeMode;
import org.devocative.wickomp.form.code.WCodeInput;
import org.devocative.wickomp.form.validator.WPatternValidator;
import org.devocative.wickomp.html.WEasyLayout;
import org.devocative.wickomp.html.WFloatTable;
import org.devocative.wickomp.html.WMessager;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.panel.SelectionPanel;
import org.devocative.wickomp.vo.Field;
import org.devocative.wickomp.vo.KeyValue;
import org.devocative.wickomp.vo.PersonVO;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class FormPage extends BasePage {

	private WEasyLayout layout;
	private WebMarkupContainer west;

	public FormPage() {
		west = new WebMarkupContainer("west");
		layout = new WEasyLayout("layout");
		layout.setWest(west);
		add(layout);

		simpleForm();
		dynamicForm();

		add(new EasyUIBehavior());
	}

	private void dynamicForm() {
		List<Field> fields = new ArrayList<>();
		fields.add(new Field("Name", "name", Field.Type.String));
		fields.add(new Field("Age", "age", Field.Type.Integer));
		fields.add(new Field("Weight", "weight", Field.Type.Real));
		fields.add(new Field("Date", "date", Field.Type.Date));
		fields.add(new Field("Alive", "alive", Field.Type.Boolean));

		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe%");
		map.put("age", 123456);

		Form<Map<String, Serializable>> dynamicForm = new Form<>("dynamicForm", new CompoundPropertyModel<>(map));

		WFloatTable floatTable = new WFloatTable("floatTable");
		dynamicForm.add(floatTable);

		floatTable.add(new ListView<Field>("fields", fields) {
			@Override
			protected void populateItem(ListItem<Field> item) {
				Field field = item.getModelObject();
				//item.add(new TextField<String>("field"));

				RepeatingView view = new RepeatingView("field");
				FormComponent fc = null;
				switch (field.getType()) {

					case String:
						fc = new WSqlStringInput(field.getName());
						break;

					case Integer:
						fc = new WNumberInput(field.getName(), Long.class)
							.setThousandSeparator(',');
						break;

					case Real:
						fc = new WNumberInput(field.getName(), BigDecimal.class)
							.setPrecision(4)
							.setThousandSeparator(',');
						break;

					case Boolean:
						fc = new WBooleanInput(field.getName());
						break;

					case Date:
						fc = new WDateInput(field.getName());
				}

				fc.setLabel(new Model<>(field.getTitle()));
				view.add(fc);

				item.add(view);
			}
		});

		//dynamicForm.add(new Button("save") {
		dynamicForm.add(new AjaxButton("save") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println(map);
			}

			/*@Override
			public void onSubmit() {
				System.out.println(map);
			}*/
		});
		layout.add(dynamicForm);
	}

	private String sql;

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
		final Map<String, Serializable> map = new HashMap<>();
//		map.put("name", "Joe");
		map.put("eduSingle", new KeyValue("A"));
		map.put("eduMultiple", (Serializable) Arrays.asList(new KeyValue("A"), new KeyValue("D")));
		map.put("child", "B.1");
		//map.put("age", new RangeVO(100, 2000));
		map.put("age", 2000);
		map.put("orderedPerson", new ArrayList<>(personVOs.subList(4, 8)));

		map.put("kvList", (Serializable) Arrays.asList(new KeyValueVO<>("A", "A"), new KeyValueVO<>("B", "B")));

		Form<Map<String, Serializable>> form = new Form<>("form", new CompoundPropertyModel<>(map));

//		form.add(new WTextInput("name").setRequired(true).add(new WAsciiIdentifierValidator()));
//		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$")));
		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$", "name.format")));
//		form.add(new WTextInput("name").setRequired(true).add(new WPatternValidator("^[A-Za-z]+?[A-Za-z0-9]*?$").setCustomMessage("oops!")));

		//form.add(new WNumberRangeInput("age", Integer.class).setThousandSeparator(","));
		form.add(new WNumberInput("age", Integer.class).setThousandSeparator(','));
		form.add(new WSelectionInput("eduSingle", list, false));
		form.add(new WSelectionInput("eduMultiple", list, true));
		form.add(new DropDownChoice<>("eduDD", list));
		form.add(new WDateInput("birthdate").setTimePartVisible(true));
		form.add(new WBooleanInput("alive"));
		//form.add(new WDateRangeInput("dateRange"));
		form.add(parentSI = new WSelectionInput("parent", Arrays.asList("A", "B", "C"), false));
		form.add(child = new WSelectionInput("child", Arrays.asList("B.1"), false));
		form.add(new WCodeInput("sql", new PropertyModel<String>(this, "sql"), oCode));
		form.add(new WClientSearchableListInput<KeyValueVO<String, String>>("kvList") {
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
			protected List<KeyValueVO<String, String>> createClientOptions(List<KeyValueVO<String, String>> list) {
				return list;
			}
		}.setOpenModalLinkVisible(true));
		form.add(new WOrderedListInput<>("orderedPerson", personVOs).setVisibleSize(10));
		form.add(new Button("save") {
			//		form.add(new WAjaxButton("save") {
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

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				theSubmit();
			}

			private void theSubmit() {
				System.out.println("MAP {{");
				for (Map.Entry<String, Serializable> entry : map.entrySet()) {
					System.out.printf("\t%s = %s (%s)\n", entry.getKey(), entry.getValue(),
						entry.getValue() != null ? entry.getValue().getClass().getName() : "-");
				}
				System.out.println("}}");

				System.out.println("sql = " + sql);
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
}
