package dad.javafx.componentes;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MonthCalendar2 extends VBox implements Initializable {
	// view
	@FXML
	private VBox root;

	@FXML
	private Label monthNameText;

	@FXML
	private GridPane calendarGrid;

	// model
	private IntegerProperty monthProperty = new SimpleIntegerProperty();
	private IntegerProperty yearProperty = new SimpleIntegerProperty();

	private String[] months = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
			"Octubre", "Noviembre", "Diciembre" };
	
	private ArrayList<Label> days = new ArrayList<Label>();
	
	public MonthCalendar2() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CalendarView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// listeners
		monthProperty.addListener((o, ov, nv) -> changeMonth(nv));
		yearProperty.addListener((o, ov, nv) -> changeYear(nv));
	}

	private void changeYear(Number nv) {
		//hay que reajustar los dias asi que llamamos al cambio de mes con el mismo mes
	
		changeMonth(getMonthProperty());
		
	}

	private void changeMonth(Number nv) {
		calendarGrid.getChildren().removeAll(days);
		days.clear();
		
		int year = yearProperty.get();

		monthNameText.setText(months[nv.intValue()-1]);
		Calendar calendar = Calendar.getInstance();

		calendar.set(year, nv.intValue() - 1, 0);

		// para que empiece en el dia que debe y no siempre en lunes
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		int day = 1;
		int row = 1;
		// numero de dias del mes dependiendo del año para no preocuparnos por bisiestos
		int numberOfDays = YearMonth.of(year, nv.intValue()).lengthOfMonth();

		// añadimos celda dia por dia
		while (day <= numberOfDays) {
			int i = 0;

			for (i = dayOfWeek; i <= 7 && day <= numberOfDays; i++) {
				
				Label dayName = new Label(String.valueOf(day));
				dayName.setId("dia");
				LocalDate currentDate = LocalDate.of(year, nv.intValue(), day);
				if(currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
					dayName.setId("domingo");
				}
				if (currentDate == LocalDate.now()) {
					dayName.setId("hoy");
				}
				calendarGrid.add(dayName, i - 1, row);
				days.add(dayName);
				day++;
			}

			dayOfWeek = 1; //lo ponemos en el primer dia de la semana al final pa que vuelva a empezar
			
			// y pasamos a la siguiente fila
			row++;
		}
	}

	public final IntegerProperty monthPropertyProperty() {
		return this.monthProperty;
	}

	public final int getMonthProperty() {
		return this.monthPropertyProperty().get();
	}

	public final void setMonthProperty(final int monthProperty) {
		this.monthPropertyProperty().set(monthProperty);
	}

	public final IntegerProperty yearPropertyProperty() {
		return this.yearProperty;
	}

	public final int getYearProperty() {
		return this.yearPropertyProperty().get();
	}

	public final void setYearProperty(final int yearProperty) {
		this.yearPropertyProperty().set(yearProperty);
	}

}
