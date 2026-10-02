package ru.mirea.fedorov.listviewapp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemInsets();

        ListView listView = findViewById(R.id.country_list_view);
        listView.setAdapter(new EventAdapter(this, createEvents()));
    }

    private void applySystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View root = findViewById(android.R.id.content);
        View list = findViewById(R.id.country_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(list, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top + actionBarHeight(), bars.right, bars.bottom);
            ((ListView) view).setClipToPadding(false);
            return insets;
        });
    }

    private int actionBarHeight() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    private List<HistoricalEvent> createEvents() {
        List<HistoricalEvent> events = new ArrayList<>();
        events.add(new HistoricalEvent("1440 — Печатный станок", "Иоганн Гутенберг запускает наборную печать в Европе.", R.drawable.ic_book));
        events.add(new HistoricalEvent("1492 — Плавание Колумба", "Экспедиция достигает островов Карибского моря.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1543 — Коперник", "Выходит «О вращениях небесных сфер»: Земля не центр мира.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1609 — Галилей и телескоп", "Первые наблюдения спутников Юпитера.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1687 — «Начала» Ньютона", "Сформулированы законы движения и всемирного тяготения.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1755 — Московский университет", "Открыт университет, предложенный Ломоносовым.", R.drawable.ic_book));
        events.add(new HistoricalEvent("1789 — Великая французская революция", "Начало переустройства политического строя Франции.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1825 — Восстание декабристов", "Выступление на Сенатской площади в Петербурге.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1837 — Дагерротипия", "Появляется практическая фотография.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1859 — Дарвин", "«Происхождение видов» формулирует естественный отбор.", R.drawable.ic_book));
        events.add(new HistoricalEvent("1861 — Отмена крепостного права", "Манифест Александра II в Российской империи.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1869 — Периодическая таблица", "Менделеев упорядочивает химические элементы.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1876 — Телефон Белла", "Первый рабочий телефонный аппарат.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1879 — Лампа Эдисона", "Практическое электрическое освещение.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1895 — Кинематограф", "Братья Люмьер показывают движение на экране.", R.drawable.ic_culture));
        events.add(new HistoricalEvent("1896 — Олимпийские игры", "Первые современные Игры в Афинах.", R.drawable.ic_culture));
        events.add(new HistoricalEvent("1903 — Полёт братьев Райт", "Первый управляемый полёт аппарата тяжелее воздуха.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1905 — Специальная относительность", "Эйнштейн публикует работу по SRT.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1917 — Октябрьская революция", "Смена власти в России.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1928 — Пенициллин", "Флеминг открывает антибиотик.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1941 — Великая Отечественная война", "Начало войны для СССР.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1945 — Победа", "Окончание Великой Отечественной войны.", R.drawable.ic_history));
        events.add(new HistoricalEvent("1953 — Структура ДНК", "Уотсон и Крик описывают двойную спираль.", R.drawable.ic_science));
        events.add(new HistoricalEvent("1957 — Спутник-1", "Первый искусственный спутник Земли.", R.drawable.ic_space));
        events.add(new HistoricalEvent("1961 — Полёт Гагарина", "Первый человек в космосе.", R.drawable.ic_space));
        events.add(new HistoricalEvent("1969 — Высадка на Луну", "Миссия Apollo 11.", R.drawable.ic_space));
        events.add(new HistoricalEvent("1971 — Email", "Рэй Томлинсон отправляет первое сетевое письмо.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1989 — Всемирная паутина", "Тим Бернерс-Ли предлагает WWW.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("1991 — Распад СССР", "Конец Советского Союза.", R.drawable.ic_history));
        events.add(new HistoricalEvent("2007 — iPhone", "Смартфон меняет повседневные интерфейсы.", R.drawable.ic_tech));
        events.add(new HistoricalEvent("2012 — Бозон Хиггса", "CERN подтверждает предсказанную частицу.", R.drawable.ic_science));
        events.add(new HistoricalEvent("2020 — COVID-19", "Пандемия ускоряет дистанционные технологии.", R.drawable.ic_history));
        events.add(new HistoricalEvent("Книга: «Война и мир»", "Толстой. План перечитать в ближайшие годы.", R.drawable.ic_book));
        events.add(new HistoricalEvent("Книга: «Преступление и наказание»", "Достоевский. Классика русской прозы.", R.drawable.ic_book));
        events.add(new HistoricalEvent("Книга: «Мастер и Маргарита»", "Булгаков. Роман, который стоит перечитывать.", R.drawable.ic_book));
        events.add(new HistoricalEvent("Книга: «Sapiens»", "Харари. Краткая история человечества.", R.drawable.ic_book));
        return events;
    }
}
