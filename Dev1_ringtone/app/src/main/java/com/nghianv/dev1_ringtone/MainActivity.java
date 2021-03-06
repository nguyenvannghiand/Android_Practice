package com.nghianv.dev1_ringtone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nghianv.dev1_ringtone.adapter.RecyclerviewRingtoneAdapter;
import com.nghianv.dev1_ringtone.model.Ringtone;
import com.nghianv.dev1_ringtone.model.ServerInfo;
import com.nghianv.dev1_ringtone.model.ServerInfoJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private List<Ringtone> mListRingtone;
	private RecyclerviewRingtoneAdapter mRingtoneAdapter;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		mListRingtone = new ArrayList<>();
		new FetchRingtoneAsyncTask().execute("https://defaultring.phungquangnam.name.vn/defaultringtones/restcache/defaultrings/fr2019secv41");
		mRingtoneAdapter = new RecyclerviewRingtoneAdapter(this, mListRingtone);

		recyclerView = findViewById(R.id.recycleview);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(mRingtoneAdapter);
	}

	private class FetchRingtoneAsyncTask extends AsyncTask<String, Void, List<Ringtone>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			findViewById(R.id.progress_bar_loading).setVisibility(View.VISIBLE);
		}

		@Override
		protected List<Ringtone> doInBackground(String... agrs) {
			URL url = null;
			String link = agrs[0];
			try {
				url = new URL(link);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				InputStream inputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder result = new StringBuilder();
				String line = reader.readLine();
				while (line != null) {
					result.append(line);
					line = reader.readLine();
				}
				List<Ringtone> ringtone = parseJson(result.toString());
				return ringtone;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private List<Ringtone> parseJson(String json) {
			List<Ringtone> ringtones = new ArrayList<>();
			try {
				Gson gson = new Gson();
				ServerInfoJson serverInfoJson = gson.fromJson(json, ServerInfoJson.class);
				ServerInfo serverInfo = serverInfoJson.getServerInfo().get(0);
				List<Ringtone> ringtoneList = serverInfo.getRingtones();
				for (int i = 0; i < ringtoneList.size(); i++) {
					Ringtone ringtone = ringtoneList.get(i);
					int id = ringtone.getId();
					String name = ringtone.getName();
					String count = ringtone.getCount();
					String path = ringtone.getPath();
					Ringtone ring = new Ringtone(id, name, count, path);
					ringtones.add(ring);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ringtones;
		}

		@Override
		protected void onPostExecute(List<Ringtone> ringtones) {
			super.onPostExecute(ringtones);
			findViewById(R.id.progress_bar_loading).setVisibility(View.GONE);
			mListRingtone.clear();
			mListRingtone.addAll(ringtones);
			mRingtoneAdapter.notifyDataSetChanged();
		}
	}
}
