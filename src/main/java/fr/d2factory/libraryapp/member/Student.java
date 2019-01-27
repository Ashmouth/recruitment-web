package fr.d2factory.libraryapp.member;

public class Student extends Member {
	
	private boolean isInFirstYear;
	private float COSTPERDAY = 0.10f;
	private float COSTPERDAYLATE = 0.15f;
	private int FREEDAYFIRSTYEAR = 15;
	private int DAYLIMITONBORROWING = 30;
	
	public Student(String name, float wallet, boolean isInFirstYear) {
		super(name, wallet);
		this.isInFirstYear = isInFirstYear;
	}

	@Override
	public void payBook(int numberOfDays) {
		
		int numberOfDaysToPay = numberOfDays;
		
		if (isInFirstYear) {
			if (numberOfDays > FREEDAYFIRSTYEAR) {
				numberOfDaysToPay = numberOfDays - FREEDAYFIRSTYEAR;
			} else {
				return;
			}
		}

		if (numberOfDays > DAYLIMITONBORROWING) {
			wallet -= ((DAYLIMITONBORROWING) * COSTPERDAY);
			wallet -= ((numberOfDays - DAYLIMITONBORROWING) * COSTPERDAYLATE);
		} else {
			wallet -= (numberOfDaysToPay * COSTPERDAY);
		}
		
	}

	@Override
	public int getDAYLIMITONBORROWING() {
		return DAYLIMITONBORROWING;
	}

}
