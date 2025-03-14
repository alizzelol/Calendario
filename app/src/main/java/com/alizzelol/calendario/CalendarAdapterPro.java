package com.alizzelol.calendario;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapterPro extends BaseAdapter {

    private Context context;
    private List<Date> days;
    private List<Evento> events;
    private Calendar currentCalendar;

    // Variables para personalización
    private int eventColorTaller = Color.rgb(100, 149, 237);
    private int eventColorCurso = Color.rgb(50, 205, 50);
    private Typeface eventFont = Typeface.DEFAULT;

    public CalendarAdapterPro(Context context, List<Date> days, List<Evento> events, Calendar currentCalendar) {
        this.context = context;
        this.days = days;
        this.events = events;
        this.currentCalendar = currentCalendar;
    }

    // Métodos para establecer los estilos personalizables
    public void setEventColorTaller(int color) {
        this.eventColorTaller = color;
    }

    public void setEventColorCurso(int color) {
        this.eventColorCurso = color;
    }

    public void setEventFont(Typeface font) {
        this.eventFont = font;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;
        if (convertView == null) {
            linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        } else {
            linearLayout = (LinearLayout) convertView;
        }

        TextView dayOfMonthTextView = linearLayout.findViewById(R.id.dayOfMonth);
        LinearLayout eventsContainer = linearLayout.findViewById(R.id.eventsContainer);
        eventsContainer.removeAllViews(); // Limpiar eventos anteriores

        Date day = days.get(position);
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(day);
        int dayOfMonth = tempCalendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.getDefault());
        String dayString = sdf.format(day);
        dayOfMonthTextView.setText(dayString);

        if (isSameMonth(day, currentCalendar.getTime())) {
            for (Evento event : events) {
                if (isSameDay(day, event.getFecha())) {
                    TextView eventTextView = new TextView(context);
                    eventTextView.setText(event.getTitulo());
                    eventTextView.setTextColor(Color.BLACK);
                    eventTextView.setTextSize(12);
                    eventTextView.setPadding(5, 2, 5, 2);

                    // Aplicar color de fondo según el tipo de evento
                    if (event.getTipo().equals("taller")) {
                        eventTextView.setBackgroundColor(eventColorTaller);
                    } else if (event.getTipo().equals("curso")) {
                        eventTextView.setBackgroundColor(eventColorCurso);
                    }

                    eventsContainer.addView(eventTextView);
                }
            }

            if (eventsContainer.getChildCount() > 0) {
                linearLayout.setBackgroundColor(Color.rgb(220, 220, 220)); // Cambiar color si hay eventos
            } else {
                linearLayout.setBackgroundColor(Color.WHITE);
            }

            dayOfMonthTextView.setTextColor(Color.BLACK);
        } else {
            linearLayout.setBackgroundColor(Color.LTGRAY);
            dayOfMonthTextView.setTextColor(Color.GRAY);
        }

        return linearLayout;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
}
