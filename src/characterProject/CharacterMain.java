package characterProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterMain {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANALYZE = 3, EARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;;

	public static void main(String[] args) {
		ArrayList<Character> list = new ArrayList<Character>();
		DBConnection dbCon = new DBConnection();
		boolean run = true;
		int no = 0;
		// while
		while (run) {
			System.out.println(
					"+========================================[ 캐릭터 생성프로그램 ]========================================+");
			System.out.println("|		 1 인적사항 | 2출력 | 3분석 | 4검색 | 5수정 | 6순위(총합순) | 7삭제 | 8탈출	  	          |");
			System.out.println(
					"+==================================================================================================+");
			System.out.print("> ");
			no = Integer.parseInt(sc.nextLine());
			// switch
			switch (no) {
			case INPUT:
				Character character = inputDataCharacter();
				// DB입력
				int rValue = dbCon.insert(character);
				if (rValue == 1) {
					System.out.println("삽입성공");
				} else {
					System.err.println("삽입실패");
				}
				break;
			case PRINT:
				ArrayList<Character> list2 = dbCon.select();
				if (list2 == null) {
					System.out.println("선택 실패!");
				} else {
					printCharacter(list2);
				}
				break;
			case ANALYZE:
				ArrayList<Character> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.out.println("선택 실패!");
				} else {
					analyzeData(list3);
				}
				break;
			case EARCH:
				String name = searchCharacter();
				ArrayList<Character> list4 = dbCon.nameSearch(name);
				if (list4.size() >= 1) {
					printCharacter(list4);
				} else {
					System.err.println("캐릭터 이름 검색 오류");
				}
				break;
			case UPDATE:
				int updateRetrunValue = 0;
				int id = inputId(); // id를 통해서 값
				Character characters = dbCon.selectId(id);
				if (characters == null) {
					System.out.println("수정오류");
				} else {
					Character updatecharacter = updataeCharacter(characters);
					updateRetrunValue = dbCon.update(updatecharacter);
				}
				if (updateRetrunValue == 1) {
					System.out.println("UPDATE 성공");
				} else {
					System.err.println("UPDATE 실패");
				}
				break;
			case SORT:
				ArrayList<Character> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.err.println("정렬 오류");
				} else {
					printScoreSort(list5);
				}
				Collections.sort(list);
				break;
			case DELETE:
				int delete = inputId();
				int deleteReurnValue = dbCon.delete(delete);
				if (deleteReurnValue == 1) {
					System.err.println("삭제 성공");
				} else {
					System.out.println("삭제 실패");
				}
				break;

			case EXIT:
				run = false;
				System.err.println("!SYSTEM : 프로그램을 종료합니다.");
				break;
			}
		}
	}

	private static void printScoreSort(ArrayList<Character> list) {
		Collections.sort(list, Collections.reverseOrder());
		System.out.println("");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "등" + "\t" + list.get(i).toString());
		}
	}

	private static Character updataeCharacter(Character characters) {
		int viability = inputGearData(characters.getName());
		characters.setViability(viability);
		int power = inputGearData(characters.getName());
		characters.setPower(power);
		int intelligence = inputGearData(characters.getName());
		characters.setIntelligence(intelligence);
		characters.calTotal();
		characters.calAvg();
		characters.calGrade();
		System.out.println("수정완료");
		return characters;
	}

	private static String inputgGender() {
		String gender = null;
		boolean flag = false;
		for (; true;) {
			try {
				System.out.print("성별(남자/여자) >> ");
				gender = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2}$");
				Matcher matcher = pattern.matcher(gender); // (genderNum == 1) ? "남성" : "여성";
				if (matcher.find() && gender.equals("남자") || gender.equals("여자")) {
					flag = true;
					break;
				}
				if (flag == false) {
					System.err.println("성별이 맞지 않습니다.");
				}

			} catch (Exception e) {
				System.err.println("성별이 맞지 않습니다.");
			}
		}
		return gender;
	}

	private static int inputGearData(String geard) {
		boolean run = true;
		int gear = 0;
		while (run) {
			System.out.print(geard + " " + ">>");
			try {
				gear = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(gear));
				if (matcher.find() && gear >= 0 && gear <= 100) {
					break;
				} else {
					System.err.println("!SYSTEM : 범위를 벗어났습니다. 다시 시작하겠습니다.");
				}
			} catch (Exception e) {
				System.err.println("!SYSTEM : 점수 입력에 오류가 발생하였습니다.");
				gear = 0;
			}
		}
		return gear;
	}

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID 입력 : ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NullPointerException e) {
				System.err.println("ID오류");
			}
		}
		return id;
	}

	private static String searchCharacter() {
		String name = null;
		name = nameMatchPattern();
		return name;
	}

	private static String nameMatchPattern() {
		String name = null;
		while (true) {
			try {
				System.out.print(" * 캐릭터 이름 : > ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{1,3}$");
				Matcher matcher = pattern.matcher(name);
				if (matcher.find()) {
					break;
				} else {
					System.err.println("!SYSTEM : 패턴이 옳바르지 않습니다. 다시 입력해주십시오");
				}
			} catch (Exception e) {
				System.err.println("!SYSTEM : 패턴이 옳바르지 않습니다. 다시 입력해주십시오");
			}
		}
		return name;
	}

	private static void analyzeData(ArrayList<Character> list) {
		for (Character data : list) {
			System.out.println(data.getName() + " >> " + "방어구: " + "\t" + data.getTotal() + "\t" + "평균: " + "\t"
					+ String.format("%.2f", data.getAvg()) + "\t" + "등급: " + data.getGrede());

		}
	}

	private static void printCharacter(ArrayList<Character> list) {
		System.out.println("id" + "\t" + "이름" + "\t" + "종족 " + "\t" + "클래스 " + "\t" + "레벨" + "\t" + "성별" + "\t" + "생존력"
				+ "\t" + "힘" + "\t" + "지능" + "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (Character data : list) {
			System.out.println(data);
		} // for

	}

	private static Character inputDataCharacter() {
		String name = nameMatchPattern();
		String tribe = inputTribe();
		String myclass = inputMyclass();
		String gender = inputgGender();
		int level = inputGearData("레벨");
		int viability = inputGearData("생존력");
		int power = inputGearData("힘");
		int intelligence = inputGearData("지능");
		Character character = new Character(name, tribe, myclass, gender, level, viability, power, intelligence);
		character.calTotal();
		character.calAvg();
		character.calGrade();
		return character;
	}

	private static String inputMyclass() {
		String myclass = null;
		boolean flag = false;
		for (; true;) {
			try {
				System.out.print(" * 클래스(타이탄/헌터/워록): > ");
				myclass = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{1,3}$");
				Matcher matcher = pattern.matcher(myclass);
				if (matcher.find() && myclass.equals("타이탄") || myclass.equals("헌터") || myclass.equals("워록")) {
					flag = true;
					break;
				}
				if (flag == false) {
					System.err.println("종족값이 맞지 않습니다.");
				}

			} catch (Exception e) {
				System.err.println("성별이 맞지 않습니다.");
			}
		}
		return myclass;
	}

	private static String inputTribe() {
		String tribe = null;
		boolean flag = false;
		for (; true;) {
			try {
				System.out.print(" * 종족(인간/엑소/각성자/민간인): > ");
				tribe = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{1,3}$");
				Matcher matcher = pattern.matcher(tribe);
				if (matcher.find() && tribe.equals("인간") || tribe.equals("엑소") || tribe.equals("각성자")
						|| tribe.equals("민간인")) {
					flag = true;
					break;
				}
				if (flag == false) {
					System.err.println("종족값이 맞지 않습니다.");
				}

			} catch (Exception e) {
				System.err.println("성별이 맞지 않습니다.");
			}
		}
		return tribe;

	}

}