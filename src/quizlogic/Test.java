package quizlogic;

public class Test {

	public static void main(String[] args) {
		FakeDataDeliverer fdd = new FakeDataDeliverer();
		for (Theme theme : fdd.themes) {
			System.out.println(theme.toString());
		}

	}

}
