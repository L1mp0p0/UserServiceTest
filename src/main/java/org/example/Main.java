package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final UserDao userDao = new UserDaoImpl();

    public static void main(String[] args) {

        try {

            while (true) {

                printMenu();

                String choice = scanner.nextLine();

                switch (choice) {

                    case "1":
                        createUser();
                        break;

                    case "2":
                        findUser();
                        break;

                    case "3":
                        findAllUsers();
                        break;

                    case "4":
                        updateUser();
                        break;

                    case "5":
                        deleteUser();
                        break;

                    case "0":
                        System.out.println("Завершение программы...");
                        return;

                    default:
                        System.out.println("Неверный пункт меню.");
                }
            }

        } finally {

            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void printMenu() {

        System.out.println();
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Получить всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void createUser() {

        try {

            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = new User(name, email, age);

            userDao.save(user);

            System.out.println("Пользователь создан:");
            System.out.println(user);

        } catch (NumberFormatException e) {

            System.out.println("Возраст должен быть числом.");

        } catch (Exception e) {

            System.out.println("Ошибка создания пользователя: "
                    + e.getMessage());
        }
    }

    private static void findUser() {

        try {

            System.out.print("Введите ID пользователя: ");

            long id = Long.parseLong(scanner.nextLine());

            User user = userDao.findById(id);

            if (user != null) {
                System.out.println(user);
            } else {
                System.out.println("Пользователь не найден.");
            }

        } catch (NumberFormatException e) {

            System.out.println("ID должен быть числом.");
        }
    }

    private static void findAllUsers() {

        List<User> users = userDao.findAll();

        if (users.isEmpty()) {

            System.out.println("Пользователи отсутствуют.");

        } else {

            System.out.println();
            System.out.println("Список пользователей:");

            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    private static void updateUser() {

        try {

            System.out.print("Введите ID пользователя: ");

            long id = Long.parseLong(scanner.nextLine());

            User user = userDao.findById(id);

            if (user == null) {

                System.out.println("Пользователь не найден.");
                return;
            }

            System.out.print("Новое имя: ");
            String name = scanner.nextLine();

            System.out.print("Новый email: ");
            String email = scanner.nextLine();

            System.out.print("Новый возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            userDao.update(user);

            System.out.println("Пользователь обновлён.");
            System.out.println(user);

        } catch (NumberFormatException e) {

            System.out.println("ID и возраст должны быть числами.");

        } catch (Exception e) {

            System.out.println("Ошибка обновления: "
                    + e.getMessage());
        }
    }

    private static void deleteUser() {

        try {

            System.out.print("Введите ID пользователя: ");

            long id = Long.parseLong(scanner.nextLine());

            userDao.delete(id);

            System.out.println("Операция удаления завершена.");

        } catch (NumberFormatException e) {

            System.out.println("ID должен быть числом.");

        } catch (Exception e) {

            System.out.println("Ошибка удаления: "
                    + e.getMessage());
        }
    }
}