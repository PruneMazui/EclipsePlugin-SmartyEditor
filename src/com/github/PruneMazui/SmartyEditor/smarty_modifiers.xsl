<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE smarty [
<!ENTITY nbsp "<xsl:text>&#160;</xsl:text>">
<!ENTITY cr "<xsl:text>&#13;</xsl:text>">
<!ENTITY sp "<xsl:text>&#32;</xsl:text>">
<!ENTITY br "<xsl:text>&lt;br&gt;</xsl:text>">
<!ENTITY bold "<xsl:text>&lt;b&gt;</xsl:text>">
<!ENTITY ebold "<xsl:text>&lt;/b&gt;</xsl:text>">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8" />

<xsl:template match="/smarty">
<xsl:param name="lang"></xsl:param>
<xsl:for-each select="modifiers/modifier">
  <xsl:value-of select="@name"/>
  &cr;
  <xsl:for-each select="parameters/parameter">
    <xsl:sort select="@position"/>
    <xsl:if test="@required='yes'">:</xsl:if>
  </xsl:for-each>
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
  <xsl:for-each select="parameters/parameter">
    <xsl:sort select="@position"/>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <xsl:if test="@required='yes'">&bold;</xsl:if>
    <xsl:value-of select="@position"/>
    <xsl:text>:</xsl:text>
    &nbsp;&nbsp;
    <xsl:value-of select="@type"/>
    <xsl:if test="@required='yes'">&ebold;</xsl:if>
    <xsl:if test="@default!=''">(Default:<xsl:value-of select="@default"/>)</xsl:if>
    &nbsp;&nbsp;
    <xsl:choose>
      <xsl:when test="desc[@lang=$lang]!=''">
        <xsl:value-of select="desc[@lang=$lang]"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="desc"/>
      </xsl:otherwise>
    </xsl:choose>
    &br;
  </xsl:for-each>
  &cr;
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
