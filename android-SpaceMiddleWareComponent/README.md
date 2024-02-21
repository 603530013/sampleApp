# android-SpaceMiddleware

[![Quality gate](https://ciq-quality.mpsa.com/api/project_badges/quality_gate?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android)

# Sonar Report

[![Couverture (TU)](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=coverage&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Anomalies](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=bugs&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Mauvaises pratiques](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=code_smells&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Lignes dupliquées (%)](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=duplicated_lines_density&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Lignes de code](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=ncloc&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Maintenabilité](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=sqale_rating&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Fiabilité](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=reliability_rating&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Risques de sécurité](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=security_hotspots&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Sécurité](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=security_rating&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android) [![Dette Technique](https://ciq-quality.mpsa.com/api/project_badges/measure?project=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android&metric=sqale_index&token=sqb_770969cae70e87e9d192fdf74555b3d8a0929f41)](https://ciq-quality.mpsa.com/dashboard?id=com.inetpsa.sp4%3ASp400Projects_SpaceMiddleware_Android)

# SpaceMiddleware 1.2.15-SNAPSHOT

**Date of creation :** 24/10/2022  **Last Update:** 20/12/2023

## 1. Introduction

The purpose of this document is to technically specify the integration of **SpaceMiddleware**
component.

## 2. Requirement

## 3.Dependencies

SpaceMiddleware depends on other framework to work:

| Component  | Version     |
|------------|-------------|
| Foundation | **1.12.25** |

## 4. Integration

The Gradle dependency is available via PSA artifactory repository.

```groovy
dependencies {
    implementation 'com.inetpsa.mmx:SpaceMiddleware:1.2.15-SNAPSHOT'
}
```

## 5. Branch name convention

the branch name should respect this regex to can be pushed to remote
![branch_naming_convention](documentation/images/branch_naming_convention.png)

## 5. Commit message convention

the commit message should respect this regex to can be accepted
![commit_message_convention](documentation/images/commit_message_convention.png)
