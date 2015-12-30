package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.form.WNumberInput;
import org.devocative.wickomp.form.WTextInput;
import org.devocative.wickomp.vo.Field;

import java.io.Serializable;
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
						view.add(new WNumberInput(field.getName()).setGroupSeparator(","));
						break;

					case Real:
						view.add(new WNumberInput(field.getName()).setPrecision(2).setGroupSeparator(","));
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
		final Map<String, Serializable> map = new HashMap<>();
		map.put("name", "Joe");

		Form<Map<String, Serializable>> form = new Form<>("form", new CompoundPropertyModel<>(map));
		form.add(new TextField<String>("name"));
		form.add(new TextField<>("age", Integer.class));
		form.add(new AjaxButton("save") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println(map);
			}
		});
		add(form);
	}
}
