package com.nghianv.rxjava6.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.nghianv.rxjava6.R;
import com.nghianv.rxjava6.network.model.Ticket;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.MyViewHolder> {
	private Context context;
	private List<Ticket> contactList;
	private TicketsAdapterListener listener;

	public TicketsAdapter(Context context, List<Ticket> contactList, TicketsAdapterListener listener) {
		this.context = context;
		this.contactList = contactList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.ticket_row, parent, false);
		return new MyViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		final Ticket ticket = contactList.get(position);
		Glide.with(context)
				.load(ticket.getAirline().getLogo())
		.apply(RequestOptions.circleCropTransform())
		.into(holder.logo);
		holder.airlineName.setText(ticket.getAirline().getName());

		holder.departure.setText(ticket.getDeparture() + " Dep");
		holder.arrival.setText(ticket.getArrival() + " Dest");

		holder.duration.setText(ticket.getFlightNumber());
		holder.duration.append("," + ticket.getDuration());
		holder.stops.setText(ticket.getNumberOfStops() + " Stops");

		if (!TextUtils.isEmpty(ticket.getInstructions())) {
			holder.duration.append(", " + ticket.getInstructions());
		}

		if (ticket.getPrice() != null) {
			holder.price.setText("₹" + String.format("%.0f", ticket.getPrice().getPrice()));
			holder.seats.setText(ticket.getPrice().getSeats() + " Seats");
			holder.loader.setVisibility(View.INVISIBLE);
		} else {
			holder.loader.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public int getItemCount() {
		return contactList.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.airline_name)
		TextView airlineName;

		@BindView(R.id.logo)
		ImageView logo;

		@BindView(R.id.numberOfStops)
		TextView stops;

		@BindView(R.id.numberOfSeats)
		TextView seats;

		@BindView(R.id.departure)
		TextView departure;

		@BindView(R.id.arrival)
		TextView arrival;

		@BindView(R.id.duration)
		TextView duration;

		@BindView(R.id.price)
		TextView price;

		@BindView(R.id.loader)
		SpinKitView loader;


		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(view -> {
				// send selected ticket in callback
				listener.onTicketSeletedClick(contactList.get(getAdapterPosition()));
			});
		}
	}

	public interface TicketsAdapterListener {
		void onTicketSeletedClick(Ticket ticket);
	}

}
