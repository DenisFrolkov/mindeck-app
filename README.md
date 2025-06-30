# Mindeck — приложение для запоминания через карточки

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-6DB33F?style=for-the-badge)
![ViewModel](https://img.shields.io/badge/ViewModel-3776AB?style=for-the-badge)
![Navigation](https://img.shields.io/badge/Navigation_Compose-FF6F00?style=for-the-badge)
![Room](https://img.shields.io/badge/Room-6DB33F?style=for-the-badge&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-4285F4?style=for-the-badge&logo=dagger&logoColor=white)
![Coroutines](https://img.shields.io/badge/Coroutines-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![WorkManager](https://img.shields.io/badge/WorkManager-795548?style=for-the-badge)
![AndroidX](https://img.shields.io/badge/AndroidX-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Modularization](https://img.shields.io/badge/Modularization-FF7043?style=for-the-badge&logoColor=white)

**Mindeck** — это pet-проект, реализующий механизм интервального повторения.  
Пользователь создаёт карточки с информацией, которую нужно запомнить, и повторяет их, выбирая степень сложности.  
В зависимости от этого рассчитывается дата следующего показа карточки, чтобы информация лучше закрепилась в памяти.

---

## 🚀 Функциональность

- Создание карточек с темой, вопросом и ответом
- Экран повторения карточек:
  - Отображение вопроса → нажатие "Показать ответ"
  - Выбор уровня сложности: "Легко", "Средне", "Сложно"
  - Алгоритмический расчёт времени следующего повторения
- Сохранение карточек (в будущем через Room)

---

## ⏳ Что планируется

### UI:
- Добавить возможность прикреплять к карточкам фото или видео

### Backend:
- Реализовать более точный алгоритм интервального повторения
- Перейти на Firebase для хранения и синхронизации данных

---

## 🧠 Цель проекта

- Практика построения Android-приложения с нуля
- Изучение Jetpack Compose и архитектуры MVVM / Clean Architecture
- Реализация логики интервального повторения
- Использование в личных целях

---

## ⚙️ Архитектура

UI (Jetpack Compose) → ViewModel (управляет состоянием экрана) → UseCases (бизнес-логика) → Repository (работа с данными)

### Модули проекта:

- `presentation` — отображение UI и управление состоянием
- `domain` — use-case'ы, модели, абстракции
- `data` — работа с базой данных и фоновые процессы (например, через WorkManager)

---

## 📦 Как запустить

1. Клонировать репозиторий:
```bash
git clone https://github.com/DenisFrolkov/mindeck-app.git
```
2. Скачать APK

## 🙋‍♂️ Обратная связь и контакты

Если у вас есть предложения, идеи или вы заметили баг — буду рад обратной связи.  
Также открыт для стажировок и предложений по Android-разработке.

[💬 Telegram](https://t.me/o2232)  
[📩 Gmail](https://github.com/DenisFrolkov)
---

## 🏁 Итоги

Проект **Mindeck** — это мой pet-проект, где я отработал практические навыки архитектуры, Jetpack Compose и управления состоянием.  
Пусть он простой, но я постарался сделать его качественно и понятно.

Спасибо, что посмотрели 🙌
