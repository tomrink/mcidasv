# These RGB functions were obtained from the JMA (Japan Meteorological
# Agency) RGB Training Library:
# https://www.jma.go.jp/jma/jma-eng/satellite/RGB_TL.html
# Note that the interpretation of the output of these functions is
# still ongoing and there is a possibility the functions may change.

# Several of these functions were obtained from the following JMA resource:
# https://www.jma.go.jp/jma/jma-eng/satellite/VLab/RGB_QG.html

# Some of these functions include a line containing the "resampleGrid"
# function.  This is done to resample the resolution of the red band
# to the highest resolution band being passed through the composite.
# The reason for this is that combineRGB returns a data object that
# is the resolution of the red band.  Doing this resampleGrid allows
# for a higher resolution display than would otherwise be available.

# AHI Natural Color RGB
def AHINaturalColorRGB(b3A, b4A, b5A):
    # red = band5; 0% to 100% rescalled to 0 to 255
    # grn = band4; 0% to 100% rescalled to 0 to 255
    # blu = band3; 0% to 100% rescalled to 0 to 255
    hr_b5A = resampleGrid(b5A, b3A)
    red = rescale(hr_b5A, 0, 100, 0, 255)
    grn = rescale(b4A, 0, 100, 0, 255)
    blu = rescale(b3A, 0, 100, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Night Microphysics RGB
def AHINightMicrophysicsRGB(b7T, b13T, b15T):
    # red = band13 - band15; 7.5K to -3K rescaled to 0 to 255
    # grn = band7 - band13; 2.9K to -7K rescaled to 0 to 255
    # blu = band13 inverted; 243.7K to 293.2K rescaled to 0 to 255
    m7 = b7T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    m15 = b15T.getMetadataMap()
    b7TM = mask(b7T, '>', 0, 1) * b7T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b15TM = mask(b15T, '>', 0, 1) * b15T
    b7TM.setMetadataMap(m7)
    b13TM.setMetadataMap(m13)
    b15TM.setMetadataMap(m15)
    red = rescale(b13TM-b15TM, 7.5, -3, 0, 255)
    grn = rescale(b7TM-b13TM, 2.9, -7, 0, 255)
    blu = rescale(b13TM, 243.7, 293.2, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Day Convective Storm RGB
def AHIDayConvectiveStormRGB(b3A, b5A, b7T, b8T, b10T, b13T):
    # red = band8 - band10; -35K to 5K rescaled to 0 to 255
    # grn = band7 - band13; -5K to 60K rescaled to 0 to 255; gamma 0.5
    # blu = band5 - band3; -75% to 25%% rescaled to 0 to 255
    m7 = b7T.getMetadataMap()
    m8 = b8T.getMetadataMap()
    m10 = b10T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    b7TM = mask(b7T, '>', 0, 1) * b7T
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b10TM = mask(b10T, '>', 0, 1) * b10T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b7TM.setMetadataMap(m7)
    b8TM.setMetadataMap(m8)
    b10TM.setMetadataMap(m10)
    b13TM.setMetadataMap(m13)
    hr_b8T = resampleGrid(b8TM, b3A)
    hr_b10T = resampleGrid(b10TM, b3A)
    red = rescale(hr_b8T-hr_b10T, -35, 5, 0, 255)
    grn = 255*(rescale(b7TM-b13TM, -5, 60, 0, 1)**2)
    blu = rescale(b5A-b3A, -75, 25, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Airmass RGB
def AHIAirmassRGB(b8T, b10T, b12T, b13T):
    # red = band10 - band8; 25.8K to 0K rescaled to 0 to 255
    # grn = band13 - band12; 41.5K to -4.3K rescaled to 0 to 255
    # blu = band8; 242.6K to 208K rescaled to 0 to 255
    m8 = b8T.getMetadataMap()
    m10 = b10T.getMetadataMap()
    m12 = b12T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b10TM = mask(b10T, '>', 0, 1) * b10T
    b12TM = mask(b12T, '>', 0, 1) * b12T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b8TM.setMetadataMap(m8)
    b10TM.setMetadataMap(m10)
    b12TM.setMetadataMap(m12)
    b13TM.setMetadataMap(m13)
    red = rescale(b10TM-b8TM, 25.8, 0, 0, 255)
    grn = rescale(b13TM-b12TM, 41.5, -4.3, 0, 255)
    blu = rescale(b8TM, 242.6, 208, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Tropical Airmass RGB
def AHITropicalAirmassRGB(b8T, b10T, b12T, b13T):
    # red = band8 - band10; -25K to 0K rescaled to 0 to 255
    # grn = band12 - band13; -25K to 25K rescaled to 0 to 255
    # blu = band8; 243K to 208K rescaled to 0 to 255
    m8 = b8T.getMetadataMap()
    m10 = b10T.getMetadataMap()
    m12 = b12T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b10TM = mask(b10T, '>', 0, 1) * b10T
    b12TM = mask(b12T, '>', 0, 1) * b12T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b8TM.setMetadataMap(m8)
    b10TM.setMetadataMap(m10)
    b12TM.setMetadataMap(m12)
    b13TM.setMetadataMap(m13)
    red = rescale(b8TM-b10TM, -25, 0, 0, 255)
    grn = rescale(b12TM-b13TM, -25, 25, 0, 255)
    blu = rescale(b8TM, 243, 208, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Ash RGB
def AHIAshRGB(b11T, b13T, b15T):
    # red = band15 - band13; -4K to 2K rescaled to 0 to 255
    # grn = band13 - band11; -4K to 5K rescaled to 0 to 255
    # blu = band13; 243K to 208K rescaled to 0 to 255
    m11 = b11T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    m15 = b15T.getMetadataMap()
    b11TM = mask(b11T, '>', 0, 1) * b11T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b15TM = mask(b15T, '>', 0, 1) * b15T
    b11TM.setMetadataMap(m11)
    b13TM.setMetadataMap(m13)
    b15TM.setMetadataMap(m15)
    red = rescale(b15TM-b13TM, -4, 2, 0, 255)
    grn = rescale(b13TM-b11TM, -4, 5, 0, 255)
    blu = rescale(b13TM, 243, 208, 0, 255)
    return combineRGB(red, grn, blu)

# AHI True Color RGB
def AHITrueColorRGB(b1A, b2A, b3A):
    # red = band3; 0% to 100% rescaled to 0 to 255
    # grn = band2; 0% to 100% rescaled to 0 to 255
    # blu = band1; 0% to 100% rescaled to 0 to 255
    red = rescale(b3A, 0, 100, 0, 255)
    grn = rescale(b2A, 0, 100, 0, 255)
    blu = rescale(b1A, 0, 100, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Dust RGB
def AHIDustRGB(b11T, b13T, b15T):
    # red = band15 - band13; -4K to 2K rescaled to 0 to 255
    # grn = band13 - band11; 0K to 15K rescaled to 0 to 255; gamma 2.5
    # blu = band13; 261K to 289K rescaled to 0 to 255
    m11 = b11T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    m15 = b15T.getMetadataMap()
    b11TM = mask(b11T, '>', 0, 1) * b11T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b15TM = mask(b15T, '>', 0, 1) * b15T
    b11TM.setMetadataMap(m11)
    b13TM.setMetadataMap(m13)
    b15TM.setMetadataMap(m15)
    red = rescale(b15TM-b13TM, -4, 2, 0, 255)
    grn = 255*(rescale(b13TM-b11TM, 0, 15, 0, 1)**0.4)
    blu = rescale(b13TM, 261, 289, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Differential Water Vapor RGB
def AHIDiffWaterVaporRGB(b8T, b10T):
    # red = band10 - band8; 30K to -3K rescaled to 0 to 255; gamma 3.5
    # grn = band10; 278.2K to 213.2K rescaled to 0 to 255; gamma 2.5
    # blu = band8; 243.9K to 208.5K rescaled to 0 to 255; gamma 2.5
    m8 = b8T.getMetadataMap()
    m10 = b10T.getMetadataMap()
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b10TM = mask(b10T, '>', 0, 1) * b10T
    b8TM.setMetadataMap(m8)
    b10TM.setMetadataMap(m10)
    red = 255*(rescale(b10TM-b8TM, 30, -3, 0, 1)**0.285714)
    grn = 255*(rescale(b10TM, 278.2, 213.2, 0, 1)**0.4)
    blu = 255*(rescale(b8TM, 243.9, 208.5, 0, 1)**0.4)
    return combineRGB(red, grn, blu)

# AHI Simple Water Vapor RGB
def AHISimpleWaterVaporRGB(b8T, b10T, b13T):
    # red = band13; 279K to 202.3K rescaled to 0 to 255; gamma 10.0
    # grn = band8; 242.7 to 214.7K rescaled to 0 to 255; gamma 5.5
    # blu = band10; 261K to 245.1K rescaled to 0 to 255; gamma 5.5
    m8 = b8T.getMetadataMap()
    m10 = b10T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b10TM = mask(b10T, '>', 0, 1) * b10T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b8TM.setMetadataMap(m8)
    b10TM.setMetadataMap(m10)
    b13TM.setMetadataMap(m13)
    red = 255*(rescale(b13TM, 279, 202.3, 0, 1)**0.1)
    grn = 255*(rescale(b8TM, 242.7, 214.7, 0, 1)**0.181818)
    blu = 255*(rescale(b10TM, 261, 245.1, 0, 1)**0.181818)
    return combineRGB(red, grn, blu)

# AHI DayDeep Clouds RGB
def AHIDayDeepCloudsRGB(b3A, b8T, b13T):
    # red = band13 - band8; 35K to -5K rescaled to 0 to 255
    # grn = band3; 70% to 100% rescaled to 0 to 255
    # blu = band13 inverted; 243.6K to 292.6K rescaled to 0 to 255
    m8 = b8T.getMetadataMap()
    m13 = b13T.getMetadataMap()
    b8TM = mask(b8T, '>', 0, 1) * b8T
    b13TM = mask(b13T, '>', 0, 1) * b13T
    b8TM.setMetadataMap(m8)
    b13TM.setMetadataMap(m13)
    hr_b8T = resampleGrid(b8TM, b3A)
    hr_b13T = resampleGrid(b13TM, b3A)
    red = rescale(hr_b13T-hr_b8T, 35, -5, 0, 255)
    grn = rescale(b3A, 70, 100, 0, 255)
    blu = rescale(hr_b13T, 243.6, 292.6, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Natural Fire Color RGB
def AHINaturalColorFireRGB(b3A, b4A, b6A):
    # red = band5; 0% to 100% rescaled to 0 to 255
    # grn = band4; 0% to 100% rescaled to 0 to 255
    # blu = band3; 0% to 100% rescaled to 0 to 255
    hr_b4A = resampleGrid(b4A, b3A)
    hr_b6A = resampleGrid(b6A, b3A)
    red = rescale(hr_b6A, 0, 100, 0, 255)
    grn = rescale(hr_b4A, 0, 100, 0, 255)
    blu = rescale(b3A, 0, 100, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Fire Temperature RGB
def AHIFireTempRGB(b5A, b6A, b7T):
    # red = band7; 273K to 350K rescaled to 0 to 255
    # grn = band6; 0% to 50% rescaled to 0 to 255
    # blu = band5; 0% to 50% rescaled to 0 to 255
    m7 = b7T.getMetadataMap()
    b7TM = mask(b7T, '>', 0, 1) * b7T
    b7TM.setMetadataMap(m7)
    hr_b7T = resampleGrid(b7TM, b5A)
    red = rescale(hr_b7T, 273, 350, 0, 255)
    grn = rescale(b6A, 0, 50, 0, 255)
    blu = rescale(b5A, 0, 50, 0, 255)
    return combineRGB(red, grn, blu)

# AHI Day Cloud Phase RGB
def AHIDayCloudPhaseRGB(b1A, b5A, b6A):
    # red = band5; 0% to 50% rescaled to 0 to 255
    # grn = band6; 0% to 50% rescaled to 0 to 255
    # blu = band1; 0% to 100% rescaled to 0 to 255
    hr_b5A = resampleGrid(b5A, b1A)
    hr_b6A = resampleGrid(b6A, b1A)
    red = rescale(hr_b5A, 0, 50, 0, 255)
    grn = rescale(hr_b6A, 0, 50, 0, 255)
    blu = rescale(b1A, 0, 100, 0, 255)
    return combineRGB(red, grn, blu)
