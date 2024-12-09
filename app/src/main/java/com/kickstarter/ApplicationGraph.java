package com.kickstarter;

import com.kickstarter.libs.Environment;
import com.kickstarter.libs.utils.ApplicationLifecycleUtil;
import com.kickstarter.services.firebase.MessageService;
import com.kickstarter.services.firebase.RegisterTokenWorker;
import com.kickstarter.services.firebase.ResetDeviceIdWorker;
import com.kickstarter.services.firebase.UnregisterTokenWorker;
import com.kickstarter.ui.views.IconTextView;
import com.kickstarter.ui.views.KSWebView;

public interface ApplicationGraph {
  Environment environment();
  void inject(ApplicationLifecycleUtil __);
  void inject(IconTextView __);
  void inject(KSWebView __);
  void inject(KSApplication __);
  void inject(MessageService __);
  void inject(RegisterTokenWorker __);
  void inject(ResetDeviceIdWorker __);
  void inject(UnregisterTokenWorker __);
}
