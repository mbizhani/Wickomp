package org.devocative.wickomp.panel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.formatter.OBooleanFormatter;
import org.devocative.wickomp.formatter.ODateFormatter;
import org.devocative.wickomp.formatter.ONumberFormatter;
import org.devocative.wickomp.grid.IGridDataSource;
import org.devocative.wickomp.grid.OGrid;
import org.devocative.wickomp.grid.WDataGrid;
import org.devocative.wickomp.grid.WSortField;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.toolbar.OExportExcelButton;
import org.devocative.wickomp.grid.toolbar.OGridGroupingButton;
import org.devocative.wickomp.html.WEasyLayout;
import org.devocative.wickomp.html.icon.FontAwesome;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.vo.PersonVO;

import java.util.List;

public class SelectionPanel extends WPanel {
	private OGrid<PersonVO> grid1Opt;

	public SelectionPanel(String id) {
		super(id);

		WebMarkupContainer west = new WebMarkupContainer("west");

		WEasyLayout layout = new WEasyLayout("layout");
		layout.setWest(west);
		add(layout);

		final List<PersonVO> list = PersonVO.list();

		OColumnList<PersonVO> columns = new OColumnList<>();
		columns
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col02"), "col02").setWidth(OSize.fixed(50)))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col01"), "col01"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 04"), "col04"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 05"), "col05"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Birth Date"), "birthDate")
				.setFormatter(ODateFormatter.prDateTime()))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Income"), "income")
				.setFormatter(ONumberFormatter.integer()))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Alive"), "alive")
				.setFormatter(OBooleanFormatter.bool()))
		;


		grid1Opt = new OGrid<>();
		grid1Opt
			.setColumns(columns)
			.setMultiSort(true)
			.setIdField("col02")
			.setTitleField("col04")
			.setSingleSelect(false)
			.setSelectionIndicator(true)
				//.setSelectionDblClick(false)
				//.setGroupField("col01")
			.addToolbarButton(new OExportExcelButton<PersonVO>(new FontAwesome("file-excel-o", new Model<>("Export to excel")).setColor("green"), "Export.xlsx", 1000))
			.addToolbarButton(new OGridGroupingButton<PersonVO>())
			.setFit(true)
		;
//		grid1Opt.setFit(true);

		layout.add(new WDataGrid<>("grid", grid1Opt, new IGridDataSource<PersonVO>() {
			@Override
			public List<PersonVO> list(long first, long size, List<WSortField> sortFields) {
				int start = (int) ((first - 1) * size);
				int end = (int) (first * size);
				return list.subList(start, Math.min(end, list.size()));
			}

			@Override
			public long count() {
				return list.size();
			}

			@Override
			public IModel<PersonVO> model(PersonVO object) {
				return new Model<>(object);
			}
		}));
	}

	public void setJS(String js) {
		grid1Opt.setSelectionJSHandler(js);
	}
}
