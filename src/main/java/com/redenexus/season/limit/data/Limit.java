package com.redenexus.season.limit.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter
public class Limit {

    private final String name;
    private final Double itemLimit;
}
