package apobooking.apobooking.com.mynotes.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apobooking.apobooking.com.mynotes.R;
import apobooking.apobooking.com.mynotes.database.NoteItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sts on 15.06.18.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NoteItem> notesList;
    private NotesListener notesListener;
    private Shader textShader;


    public NotesAdapter(int color)
    {
        notesList = new ArrayList<>();
        textShader = new LinearGradient(
                0, 100, 0, 0,
              color, Color.BLACK,
                Shader.TileMode.CLAMP );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteItem noteItem = notesList.get(position);

        holder.title.setText(noteItem.getTitle());
        if(!TextUtils.isEmpty(noteItem.getPassword()))
            holder.lockImageView.setVisibility(View.VISIBLE);
        else {
            holder.content.setText(noteItem.getText());
            holder.content.getPaint().setShader(textShader);
        }

        holder.noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.noteSelected(noteItem.getId());
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.noteSetPassword(noteItem,  holder.noteLayout.getY());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notesList.size() == 0)
        {
            return 0;
        }
        else return notesList.size();
    }

    public void setNotesList(List<NoteItem> notesList)
    {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        NoteItem noteItem = notesList.get(position);
        notesListener.noteDelete(noteItem);
        notesList.remove(position);
        notifyItemRemoved(position);
        if(notesList.size() == 0)
            notesListener.updateNoNotesTitle();
    }

    public void refresh(int itemId){
        for(int i = 0; i < notesList.size(); i++)
        {
            if(notesList.get(i).getId() == itemId)
                notifyItemChanged(i);
        }
    }

    public interface NotesListener{
        void noteSelected(int id);
        void noteDelete(NoteItem noteItem);
        void noteSetPassword(NoteItem noteItem, float y);
        void updateNoNotesTitle();
    }

    public void setNotesListener(NotesListener notesListener)
    {
        this.notesListener = notesListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTextView)
        TextView title;

        @BindView(R.id.contentTextView)
        TextView content;

        @BindView(R.id.noteLayout)
        ViewGroup noteLayout;

        @BindView(R.id.imageMenu)
        ImageView menu;

        @BindView(R.id.lockImageView)
        ImageView lockImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
