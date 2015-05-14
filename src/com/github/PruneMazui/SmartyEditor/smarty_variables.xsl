<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE smarty [
<!ENTITY cr "<xsl:text>&#13;</xsl:text>">
<!ENTITY sp "<xsl:text>&#32;</xsl:text>">
<!ENTITY br "<xsl:text>&lt;br&gt;</xsl:text>">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8" />

<xsl:template match="/smarty">
<xsl:param name="lang"></xsl:param>
<xsl:for-each select="smarty_variable/variable">
  <xsl:text>smarty.</xsl:text><xsl:value-of select="@name"/>
  &cr;
  <xsl:choose>
    <xsl:when test="desc[@lang=$lang]!=''">
      <xsl:value-of select="desc[@lang=$lang]"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="desc"/>
    </xsl:otherwise>
  </xsl:choose>
  &cr;
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
