package com.vogella.maven.quickstart;

import java.awt.DisplayMode;

public class facebooksample {
	private static final String APP_ID = "###########";
	private static final String PERMISSIONS =
	        "COMMA SEPARATED LIST OF REQUESTED PERMISSIONS";
	private String access_token;
	private long expirationTimeMillis;
	/**
	 * Implements facebook's authentication flow to obtain an access token. This
	 * method displays an embedded browser and defers to facebook to obtain the
	 * user's credentials.
	 * According to facebook, the request as we make it here should return a
	 * token that is valid for 60 days. That means this method should be called
	 * once every sixty days.
	 */
	private void authenticationFlow() {
	    DisplayMode display = new DisplayMode();
	    Shell shell = new Shell(display);
	    final Browser browser;
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 3;
	    shell.setLayout(gridLayout);

	    try {
	        browser = new Browser(shell, SWT.NONE);
	    } catch (SWTError e){
	        System.err.println("Could not instantiate Browser: " + e.getMessage());
	        display.dispose();
	        display = null;
	        return;
	    }
	    browser.setJavascriptEnabled(true);

	    GridData data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 3;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    browser.setLayoutData(data);

	    final ProgressBar progressBar = new ProgressBar(shell, SWT.MOZILLA);
	    data = new GridData();
	    data.horizontalAlignment = GridData.END;
	    progressBar.setLayoutData(data);

	    /* Event Handling */
	    browser.addProgressListener(new ProgressListener(){
	        public void changed(ProgressEvent event){
	            if(event.total == 0) return;
	            int ratio = event.current * 100 / event.total;
	            progressBar.setSelection(ratio);
	        }
	        public void completed(ProgressEvent event) {
	            progressBar.setSelection(0);
	        }
	    });
	    browser.addLocationListener(new LocationListener(){
	        public void changed(LocationEvent e){
	            // Grab the token if the browser has been redirected to
	            // the login_success page
	            String s = e.location;
	            String token_identifier = "access_token=";
	            if(s.contains("https://www.facebook.com/connect/login_success.html#access_token=")){
	                access_token = s.substring(s.lastIndexOf(token_identifier)+token_identifier.length(),s.lastIndexOf('&'));
	                String expires_in = s.substring(s.lastIndexOf('=')+1);
	                expirationTimeMillis = System.currentTimeMillis() + (Integer.parseInt(expires_in) * 1000);
	            }
	        }
	        public void changing(LocationEvent e){}
	    });

	    if(display != null){
	        shell.open();
	        browser.setUrl("https://www.facebook.com/dialog/oauth?"
	                + "client_id=" + APP_ID
	                + "&redirect_uri=https://www.facebook.com/connect/login_success.html"
	                + "&scope=" + PERMISSIONS
	                + "&response_type=token");
	        while(!shell.isDisposed()) {
	            if(!display.readAndDispatch()){
	                display.sleep();
	                if(access_token != null && !access_token.isEmpty()){
	                    try{ Thread.sleep(3000);}catch(Exception e){}
	                    shell.dispose();
	                }
	            }
	        }
	        display.dispose();
	    }
	}
}
