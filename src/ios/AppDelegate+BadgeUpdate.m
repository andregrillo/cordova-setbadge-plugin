// AppDelegate+BadgeUpdate.m

#import "AppDelegate+BadgeUpdate.h"
#import <objc/runtime.h>

@implementation AppDelegate (BadgeUpdate)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class class = [self class];

        SEL originalSelector = @selector(application:didReceiveRemoteNotification:fetchCompletionHandler:);
        SEL swizzledSelector = @selector(xxx_application:didReceiveRemoteNotification:fetchCompletionHandler:);

        Method originalMethod = class_getInstanceMethod(class, originalSelector);
        Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);

        BOOL didAddMethod =
            class_addMethod(class,
                            originalSelector,
                            method_getImplementation(swizzledMethod),
                            method_getTypeEncoding(swizzledMethod));

        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledSelector,
                                method_getImplementation(originalMethod),
                                method_getTypeEncoding(originalMethod));
        } else {
            method_exchangeImplementations(originalMethod, swizzledMethod);
        }
    });
}

- (void)xxx_application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    // Check if shouldIncrementBadge is true in the payload
    if ([userInfo objectForKey:@"shouldIncrementBadge"] && [[userInfo objectForKey:@"shouldIncrementBadge"] boolValue]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            application.applicationIconBadgeNumber += 1; // Increment the badge number
        });
    } else if ([userInfo objectForKey:@"badge"]) {
        // If shouldIncrementBadge is false or not present, and badge is present
        NSNumber *badgeNumber = [userInfo objectForKey:@"badge"];
        dispatch_async(dispatch_get_main_queue(), ^{
            application.applicationIconBadgeNumber = [badgeNumber integerValue]; // Sets specific badge number
        });
    }

    // Call the original method
    [self xxx_application:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
}


@end
