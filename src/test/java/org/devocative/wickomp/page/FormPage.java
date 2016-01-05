package org.devocative.wickomp.page;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.form.*;
import org.devocative.wickomp.vo.Field;
import org.devocative.wickomp.vo.KeyValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormPage extends BasePage {

	public FormPage() {
		simpleForm();
		dynamicForm();
	}

	private void dynamicForm() {
		List<Field> fields = new ArrayList<>();
		fields.add(new Field("Name", "name", Field.Type.String));
		fields.add(new Field("Age", "age", Field.Type.Integer));
		fields.add(new Field("Weight", "weight", Field.Type.Real));

		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe");
		map.put("age", 123456);

		Form<Map<String, Serializable>> dynamicForm = new Form<>("dynamicForm", new CompoundPropertyModel<>(map));

		dynamicForm.add(new ListView<Field>("fields", fields) {
			@Override
			protected void populateItem(ListItem<Field> item) {
				Field field = item.getModelObject();
				item.add(new Label("label", field.getTitle()));
				//item.add(new TextField<String>("field"));

				RepeatingView view = new RepeatingView("field");
				switch (field.getType()) {

					case String:
						view.add(new WTextInput(field.getName()));
						break;

					case Integer:
						view.add(new WNumberInput(field.getName(), Long.class).setThousandSeparator(","));
						break;

					case Real:
						view.add(new WNumberInput(field.getName(), BigDecimal.class).setPrecision(4).setThousandSeparator(","));
						break;

					case Boolean:
						break;
				}

				item.add(view);
			}
		});

		dynamicForm.add(new Button("save") {

			@Override
			public void onSubmit() {
				System.out.println(map);
			}
		});
		add(dynamicForm);
	}

	private void simpleForm() {
		List<KeyValue> list = new ArrayList<>();
		list.add(new KeyValue("A", "Alef"));
		list.add(new KeyValue("B", "Be"));
		list.add(new KeyValue("C", "Cee"));
		list.add(new KeyValue("D", "Dee"));


		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe");

		Form<Map<String, Serializable>> form = new Form<>("form", new CompoundPropertyModel<>(map));
		form.add(new TextField<String>("name"));
		form.add(new TextField<>("age", Integer.class));
		form.add(new WSelectionInput("eduSingle", list, false));
		form.add(new WSelectionInput("eduMultiple", list, true));
		form.add(new DropDownChoice("eduDD", list));
		form.add(new WDateInput("birthdate").setTimePartVisible(true));
		form.add(new WBooleanInput("alive"));
		form.add(new WDateRangeInput("dateRange"));
		form.add(new Button("save") {
			/*@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println(map);
			}*/

			@Override
			public void onSubmit() {
				System.out.println("MAP {{");
				for (Map.Entry<String, Serializable> entry : map.entrySet()) {
					System.out.printf("\t%s = %s (%s)\n", entry.getKey(), entry.getValue(),
						entry.getValue() != null ? entry.getValue().getClass().getName() : "-");
				}
				System.out.println("}}");
			}
		});
		add(form);
	}
}
