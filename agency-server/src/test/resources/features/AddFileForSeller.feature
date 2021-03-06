#language: ru

Функция: Добавление файла продавцу
  Администратор может изменять и добавлять атирбуты пользователю

  Предыстория:
    Допустим пользвователь авторизировался как менеджер c логином "testManager" и паролем "100" на странице "/login"

  Сценарий: Добавление файла менеджером
    Когда менеджер перейдет на "/managers/clients/sellers"
    Тогда открвыется страница продавцов
    И менеджер выбирает файл для добавления к первому в списке продавцу
    И нажимает кнопку сохранить файл
    Тогда у продавца появляется в списке файлов новый файл