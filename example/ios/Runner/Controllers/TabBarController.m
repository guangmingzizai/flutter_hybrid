//
//  TabBarController.m
//  Runner
//
//  Created by JianFei Wang on 2019/8/27.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import "TabBarController.h"
#import "NativeViewController.h"
#import <flutter_hybrid/FLHFlutterContainerViewController.h>
#import <flutter_hybrid/FLHFlutterHybridViewController.h>
#import "NavigationController.h"

@interface TabBarController ()

@end

@implementation TabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NativeViewController *nativeVC = [NativeViewController new];
    nativeVC.title = @"Native";
    nativeVC.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"NativePage" image:[[UIImage imageNamed:@"apple_icon"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] tag:1];
    UINavigationController *nativeNav = [[NavigationController alloc] initWithRootViewController:nativeVC];
    
    FLHFlutterContainerViewController *flutterVC = [[FLHFlutterContainerViewController alloc] initWithRoute:@"/flutterPage" params:nil];
    flutterVC.title = @"Flutter";
    flutterVC.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"FlutterPage" image:[[UIImage imageNamed:@"flutter_icon"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] tag:2];
    UINavigationController *flutterNav = [[NavigationController alloc] initWithRootViewController:flutterVC];
    
    self.viewControllers = @[ nativeNav, flutterNav ];
}

@end
