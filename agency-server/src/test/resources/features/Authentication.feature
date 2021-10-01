#language: ru

Функция: Аунтефикация

  Структура сценария: : Ввод пароля и логина для аунтефикации
    Пусть я захожу на страницу аунтефикации ""
    Когда я ввожу логин "<login>"
    И я ввожу пароль "<password>"
    Тогда меня перенаправляет "<url>"
    Примеры:
      | login       | password | url               |
      | testAdmin   | 100      | /only_for_admins   |
      | testManager | 100      | /managers/objects |
      | testUser    | 100      | /after_login      |