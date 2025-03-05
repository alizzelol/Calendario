package com.alizzelol.calendario;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CalendarAdapterPro extends BaseAdapter {

    private Context context;
    private List<String> days;
    private Calendar calendar;
    private Map<Integer, String> events;
    private Map<Integer, String> eventTypes;

    // Variables para personalización
    private int eventColorTaller = Color.BLUE;
    private int eventColorCurso = Color.GREEN;
    private Typeface eventFont = Typeface.DEFAULT;

    public CalendarAdapterPro(Context context, List<String> days, Map<Integer, String> events, Map<Integer, String> eventTypes) {
        this.context = context;
        this.days = days;
        this.events = events;
        this.eventTypes = eventTypes;
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
        TextView textView;
        if (convertView == null) {
            textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        } else {
            textView = (TextView) convertView;
        }

        String day = days.get(position);
        textView.setText(day);

        if (!day.isEmpty()) {
            int dayOfMonth = Integer.parseInt(day);
            if (events.containsKey(dayOfMonth)) {
                String eventType = eventTypes.get(dayOfMonth);
                if (eventType != null) {
                    if (eventType.equals("taller")) {
                        textView.setBackgroundColor(Color.BLUE);
                    } else if (eventType.equals("curso")) {
                        textView.setBackgroundColor(Color.GREEN);
                    }
                    textView.setTextColor(Color.BLACK);
                }
            } else {
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
            }
        } else {
            textView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
        }

        return textView;
    }


    private String getEventType(String eventTitle) {
        if (eventTitle.toLowerCase().contains("taller")) {
            return "taller";
        } else if (eventTitle.toLowerCase().contains("curso")) {
            return "curso";
        } else {
            return "otro";
        }
    }
}
