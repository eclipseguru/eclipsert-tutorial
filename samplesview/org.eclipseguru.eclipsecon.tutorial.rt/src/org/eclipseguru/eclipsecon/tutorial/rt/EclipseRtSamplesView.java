/**
 * Copyright (c) 2009 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.eclipseguru.eclipsecon.tutorial.rt;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import org.eclipsercp.book.tools.BundleLocation;
import org.eclipsercp.book.tools.Sample;
import org.eclipsercp.book.tools.SamplesModel;
import org.eclipsercp.book.tools.Utils;
import org.eclipsercp.book.tools.actions.CompareSamplesOperation;
import org.eclipsercp.book.tools.actions.ImportSampleOperation;
import org.eclipsercp.book.tools.views.SamplesLabelProvider;

/**
 * View for the SSE examples.
 */

public class EclipseRtSamplesView extends ViewPart {

	private static final String SAMPLES_FOLDER = "/samples";

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipseguru.eclipsecon.tutorial.rt.samples.view";

	private static final int BTN_ID_IMPORT = 101;
	private static final int BTN_ID_COMPARE = 102;

	private TableViewer projectsList;
	private Action importAction;
	private Action compareAction;

	SamplesModel samplesModel;

	/**
	 * The constructor.
	 */
	public EclipseRtSamplesView() {
	}

	void buttonPressed(final int buttonId) {
		switch (buttonId) {
			case BTN_ID_IMPORT:
				createProjects();
				projectsList.update(samplesModel.getSamples(), null);
				break;
			case BTN_ID_COMPARE:
				new CompareSamplesOperation(samplesModel).run(getSelection());
				break;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// create the parent
		final Composite workArea = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		workArea.setLayout(layout);
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		// create the list of projects
		createProjectsList(workArea);
		Dialog.applyDialogFont(workArea);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(projectsList.getControl(), "net.sourceforge.sse_examples.samples.viewer");

		updateProjectsList();

		projectsList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				buttonPressed(BTN_ID_IMPORT);
			}
		});
		fillActionBars(projectsList.getControl());

	}

	private void createProjects() {
		final ImportSampleOperation operation = new ImportSampleOperation(getShell(), getSelection(), samplesModel, true);
		Utils.run(getShell(), operation);
	}

	private void createProjectsList(final Composite parent) {
		final Label title = new Label(parent, SWT.NONE);
		title.setText("Sample Projects:");

		projectsList = new TableViewer(parent, SWT.BORDER | SWT.SINGLE);
		final GridData listData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		listData.heightHint = 125;
		listData.widthHint = 100;
		projectsList.getControl().setLayoutData(listData);
		projectsList.setSorter(new ViewerSorter() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final Sample c1 = (Sample) e1;
				final Sample c2 = (Sample) e2;
				return c1.getNumber().compareTo(c2.getNumber());
			}
		});
		projectsList.setContentProvider(new ArrayContentProvider());
		projectsList.setLabelProvider(new SamplesLabelProvider());
	}

	protected void fillActionBars(final Control control) {
		final IActionBars bars = getViewSite().getActionBars();
		importAction = new Action("&Import") {
			@Override
			public void run() {
				buttonPressed(BTN_ID_IMPORT);
			}
		};

		importAction.setToolTipText("Import the selected sample code into the Eclipse workspace.");
		compareAction = new Action("&Compare with Workspace") {
			@Override
			public void run() {
				buttonPressed(BTN_ID_COMPARE);
			}
		};

		compareAction.setToolTipText("Compare the projects in your workspace with the currently selected sample code.");
		final IToolBarManager toolbar = bars.getToolBarManager();
		toolbar.add(importAction);
		toolbar.add(compareAction);
		final IMenuManager menu = bars.getMenuManager();
		menu.add(importAction);
		menu.add(compareAction);
		final MenuManager contextMenu = new MenuManager();
		contextMenu.add(importAction);
		contextMenu.add(compareAction);
		control.setMenu(contextMenu.createContextMenu(control));
	}

	private Sample getSelection() {
		final IStructuredSelection selected = (IStructuredSelection) projectsList.getSelection();
		if ((selected != null) && (selected.size() == 1)) {
			return (Sample) (selected).getFirstElement();
		}
		return null;
	}

	Shell getShell() {
		return getSite().getShell();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		projectsList.getControl().setFocus();
	}

	/**
	 * Update the list of projects based on path
	 * 
	 * @param path
	 */
	protected void updateProjectsList() {
		final Job job = new Job("Loading examples...") {
			@Override
			protected org.eclipse.core.runtime.IStatus run(final IProgressMonitor monitor) {
				final BundleLocation location = new BundleLocation(EclipseRtSamplesPlugin.getDefault().getBundle(), new Path(SAMPLES_FOLDER));
				samplesModel = new SamplesModel();
				samplesModel.init(location, monitor);
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if ((projectsList != null) && !projectsList.getControl().isDisposed()) {
							projectsList.setInput(samplesModel.getSamples());
						}
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}