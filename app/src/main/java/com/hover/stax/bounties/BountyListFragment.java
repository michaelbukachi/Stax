package com.hover.stax.bounties;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hover.sdk.actions.HoverAction;
import com.hover.stax.R;
import com.hover.stax.utils.UIHelper;
import com.hover.stax.views.StaxDialog;

public class BountyListFragment extends Fragment implements BountyListAdapter.SelectListener {
	private static final String TAG = "BountyListFragment";
	private BountyViewModel bountyViewModel;
	private View view;
	private RecyclerView bountyRecyclerView;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bounty_list_layout, container, false);
		bountyViewModel = new ViewModelProvider(this).get(BountyViewModel.class);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initRecyclerView();
		startObservers();
	}

	private void initRecyclerView() {
		bountyRecyclerView = view.findViewById(R.id.bountyList_recyclerView_id);
		bountyRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
	}

	private void startObservers() {
		bountyViewModel.getActions().observe(getViewLifecycleOwner(), actions -> Log.e(TAG, "actions update: " + actions.size()));
		bountyViewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> Log.e(TAG, "transactions update: " + transactions.size()));
		bountyViewModel.getMap().observe(getViewLifecycleOwner(), actionTransactionsMap -> {
			if (actionTransactionsMap != null && actionTransactionsMap.size() > 0) {
				BountyListAdapter bountyListAdapter = new BountyListAdapter(actionTransactionsMap, this);
				bountyRecyclerView.setAdapter(bountyListAdapter);
			}
		});
	}

	@Override
	public void viewTransactionDetail(String uuid) {

	}

	@Override
	public void bountyDetail(Bounty b) {
		Log.e(TAG, "showing dialog " + b.action);
		new StaxDialog(requireActivity())
			.setDialogTitle(getString(R.string.bounty_claim_title, b.action.root_code, b.action.getHumanFriendlyType(getContext(), b.action.transaction_type), b.action.bounty_amount))
			.setDialogMessage(getString(R.string.bounty_claim_explained, b.action.bounty_amount, b.getInstructions(getContext())))
			.setPosButton(R.string.start_USSD_Flow, v -> {
				if (getActivity() != null)
					((BountyActivity) getActivity()).makeCall(b.action);
			})
			.showIt();
	}
}
