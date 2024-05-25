
# Некоторые моменты, которые стоит уточнить

В задании в пункте 2 указаны все данные, которые необходимы для ввода и последующйе регистрации, но не указаны ФИО и дата рождения. В проекте создание аккаунта предполагает их обязательное наличие в теле объекта запроса

---

В задании указано, что доступ должен быть ограничен (служебное и общедоступное апи). Предполагается что поиск по пользователям может производить любой зарегестрированный пользователь. Поэтому в проекте это указано только в конфигурации `HttpSecurity`.

Строки для запрета просмотра "не своего профиля" также **не** были добавлены:

`@PreAuthorize("#id == authentication.principal.customer.id")`

---

Некоторые маппинги, например поиск по фильтру, возвращает `HttpStatus.OK` даже если ни одного совпадения не найдено. При этом будет возвращен пустой список. 

---

Каскадирование Hibernate отключено в силу `SAVE_UPDATE -> deprecated `. Каскадирование JPA не даст удалить "ребенка", в нашем случае телефон/емаил.

---

Вместо `SQL Constraints` и подобного подхода :
```
@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
@CollectionTable(name = "books", joinColumns = @JoinColumn(name = "library_id"))
@Column(name = "book", nullable = false)
private List<String> books = new ArrayList<>();
```
было решено сделать стандартное отношение `@OneToMany`. Это позволит выполнять поиск только номера/емаила проще + можно расширить, допустим, назначить рассылку и восстановление пароля на разные email, помечать email как "недействительный" и т.д.

---

Scheduled таск находится в `event` и выполняет кастомный запрос из CustomerDAO.

---

Для самых необходимых валидаций настроены валидаторы, помимо @Valid. Кастомные запросы **пока что** сделаны только для CustomerValidator. 

---

ТЗ: [ссылка на документ с ТЗ](https://docs.google.com/document/d/1NIe1ju0e0Gb1otPgVQKeQvpICBzWrtPPDJjaFG2Ybgw/edit?usp=sharing)
