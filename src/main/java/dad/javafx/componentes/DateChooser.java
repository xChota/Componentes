package dad.javafx.componentes;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DateChooser extends HBox implements Initializable {
	// model
	private ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<LocalDate>();
	private ArrayList<String> monthList = new ArrayList<String>(Arrays.asList("Enero", "Febrero", "Marzo", "Abril",
			"Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
	private ChangeListener<String> changeListener;
	// FXML view
	@FXML
	private ComboBox<Integer> dayCombo;

	@FXML
	private ComboBox<String> monthCombo;

	@FXML
	private ComboBox<String> yearCombo;

	public DateChooser() {
		super();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DateChooserView.fxml"));
			loader.setController(this);
			loader.setRoot(this); // establecer la rootView
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getMonthCombo().getItems().addAll(monthList);
		getMonthCombo().getSelectionModel().selectedItemProperty().addListener(e -> changeMonth());
		getYearCombo().getItems().addAll(getYears());
		getYearCombo().getSelectionModel().selectedItemProperty().addListener(e -> changeMonth());
		changeListener = new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        onlyNumeric();
		    }
		};
		getYearCombo().getEditor().textProperty().addListener(changeListener);
		dateProperty.set(LocalDate.MIN);
		
		getMonthCombo().getSelectionModel().selectedItemProperty().addListener(e -> changeDate(1));
		getDayCombo().getSelectionModel().selectedItemProperty().addListener(e -> changeDate(2));
		getYearCombo().getSelectionModel().selectedItemProperty().addListener(e -> changeDate(3));
		
		dateProperty.addListener(e -> setCombos());
	}



	private void setCombos() {
		int month = dateProperty.get().getMonthValue();
		int day = dateProperty.get().getDayOfMonth();
		int year = dateProperty.get().getYear();
		
		getYearCombo().getSelectionModel().select(year+"");
		getMonthCombo().getSelectionModel().select(month-1);
		getDayCombo().getSelectionModel().select(day-1);
		
	}

	private void changeDate(int cod) {
		switch (cod) {
		case 1:
			int month = getMonthCombo().getSelectionModel().getSelectedIndex() + 1;
			dateProperty.set(dateProperty.get().withMonth(month));			
		
			break;

		case 2:
			try {
				int day = getDayCombo().getSelectionModel().getSelectedItem();
				dateProperty.set(dateProperty.get().withDayOfMonth(day));
				
			} catch (NullPointerException e) {
				
			}
			break;
		
		case 3:
			int year = Integer.parseInt(getYearCombo().getSelectionModel().getSelectedItem());
			dateProperty.set(dateProperty.get().withYear(year));
			
			break;
		}
	}

	private void onlyNumeric() {
		StringBuilder text = new StringBuilder(getYearCombo().getEditor().textProperty().get());

		if (text.length() > 0) {
			for (int i = 0; i < text.length(); i++) {
				if (!Character.isDigit(text.charAt(i))) {
					text.deleteCharAt(i);
				}
			}
			getYearCombo().getEditor().textProperty().removeListener(changeListener);
			getYearCombo().getEditor().textProperty().set(text.toString());
			getYearCombo().getEditor().textProperty().addListener(changeListener);
		}
	}

	private ArrayList<String> getYears() {
		ArrayList<String> yearList = new ArrayList<>();
		for (int year = Year.now().getValue(); year >= 1900; year--) {
			yearList.add(year + "");
		}

		return yearList;
	}

	private void changeMonth() {
		int month = getMonthCombo().getSelectionModel().getSelectedIndex();
		getDayCombo().getItems().clear();
		String year = "";
		if (getYearCombo().getSelectionModel().getSelectedItem() != null) {
			year = getYearCombo().getSelectionModel().getSelectedItem();
		}

		if (month == 1) {
			
			if (year == "" || !Year.of(Integer.parseInt(year)).isLeap()) {
				for (int i = 1; i < 29; i++) {
					getDayCombo().getItems().add(i);
				}
			} else {
				
				for (int i = 1; i <= 29; i++) {
					getDayCombo().getItems().add(i);
				}
			}
		} else if (month == 0 | month == 2 | month == 4 | month == 6 | month == 7 | month == 9 | month == 11) {
		
			for (int i = 1; i <= 31; i++) {
				getDayCombo().getItems().add(i);
			}
		} else {
		
			for (int i = 1; i < 31; i++) {
				getDayCombo().getItems().add(i);
			}
		}

	}

	public final ObjectProperty<LocalDate> datePropertyProperty() {
		return this.dateProperty;
	}
	

	public final LocalDate getDateProperty() {
		return this.datePropertyProperty().get();
	}
	

	public final void setDateProperty(final LocalDate dateProperty) {
		this.datePropertyProperty().set(dateProperty);
	}

	public ComboBox<String> getMonthCombo() {
		return monthCombo;
	}

	public void setMonthCombo(ComboBox<String> monthCombo) {
		this.monthCombo = monthCombo;
	}

	public ComboBox<Integer> getDayCombo() {
		return dayCombo;
	}

	public void setDayCombo(ComboBox<Integer> dayCombo) {
		this.dayCombo = dayCombo;
	}

	public ComboBox<String> getYearCombo() {
		return yearCombo;
	}

	public void setYearCombo(ComboBox<String> yearCombo) {
		this.yearCombo = yearCombo;
	}
	
}
