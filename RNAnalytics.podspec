
Pod::Spec.new do |s|
  s.name         = "RNAnalytics"
  s.version      = "1.8.0"
  s.summary      = "RNAnalytics"
  s.description  = <<-DESC
                  RNAnalytics
                   DESC
  s.homepage     = "https://github.com/fangasvsass/react-native-baidu-analytics"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/fangasvsass/react-native-baidu-analytics.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "BaiduMobStatCodeless"

end

  
