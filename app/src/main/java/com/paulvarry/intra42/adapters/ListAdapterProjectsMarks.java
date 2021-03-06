package com.paulvarry.intra42.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.paulvarry.intra42.R;
import com.paulvarry.intra42.api.model.ProjectsLTE;
import com.paulvarry.intra42.api.model.ProjectsUsers;
import com.paulvarry.intra42.utils.AppSettings;
import com.paulvarry.intra42.utils.ProjectUserStatus;

import java.util.List;

public class ListAdapterProjectsMarks extends BaseAdapter {

    private final Context context;
    private SparseArray<ProjectsUsers> projectsUsers; // index by project id
    private List<ProjectsLTE> projects;

    public ListAdapterProjectsMarks(Context context, List<ProjectsLTE> projectsList) {

        this.context = context;
        projects = projectsList;
    }

    public void setProjectUser(List<ProjectsUsers> projectsUsers) {
        if (this.projectsUsers == null)
            this.projectsUsers = new SparseArray<>();
        for (ProjectsUsers p : projectsUsers) {
            this.projectsUsers.append(p.project.id, p);
        }
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return projects.size();
    }

    /**
     * Get the data projectsList associated with the specified position in the data set.
     *
     * @param position Position of the projectsList whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public ProjectsLTE getItem(int position) {
        return projects.get(position);
    }

    @Nullable
    public ProjectsUsers getProjectUser(ProjectsLTE project) {
        if (projectsUsers != null)
            return projectsUsers.get(project.id);
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the projectsList within the adapter's data set whose row id we want.
     * @return The id of the projectsList at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (vi == null)
                return null;
            convertView = vi.inflate(R.layout.list_view__detail_summary, parent, false);

            holder.textViewProjectName = convertView.findViewById(R.id.textViewTitle);
            holder.textViewProjectSlug = convertView.findViewById(R.id.textViewSummary);
            holder.textViewProjectMark = convertView.findViewById(R.id.textViewDetail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProjectsLTE project = getItem(position);
        @Nullable
        ProjectsUsers projectUser = getProjectUser(project);

        String title = project.name;

        if (project instanceof ProjectsUsers.ProjectsLTE) {
            ProjectsUsers.ProjectsLTE tmp = (ProjectsUsers.ProjectsLTE) project;
            if (tmp.parentId != null && projects.get(tmp.parentId) != null)
                title = projects.get(tmp.parentId).name + " - " + title;
        }
        holder.textViewProjectName.setText(title);
        holder.textViewProjectSlug.setText(project.slug);
        ProjectUserStatus.setMark(context, projectUser, holder.textViewProjectMark);

        if (AppSettings.Advanced.getAllowAdvancedData(context))
            holder.textViewProjectSlug.setVisibility(View.VISIBLE);
        else
            holder.textViewProjectSlug.setVisibility(View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        private TextView textViewProjectName;
        private TextView textViewProjectSlug;
        private TextView textViewProjectMark;
    }
}