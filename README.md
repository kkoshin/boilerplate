### application-template

### Dependencies management
- gradle.properties: 
  - kotlin version
  - compose version
  - agp version
  - sdk version
- libs.version.toml: common dependencies
- android.versions.toml: android dependencies
- desktop.versions.toml: desktop dependencies

### known issue
`@Preview` can't be used in the commonMain module 