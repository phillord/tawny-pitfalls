;; The contents of this file are subject to the LGPL License, Version 3.0.

;; Copyright (C) 2013, Phillip Lord, Newcastle University

;; This program is free software: you can redistribute it and/or modify it
;; under the terms of the GNU Lesser General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or (at your
;; option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT
;; ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
;; FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
;; for more details.

;; You should have received a copy of the GNU Lesser General Public License
;; along with this program. If not, see http://www.gnu.org/licenses/.


(ns tawny.pitfalls.only
  [:use [tawny.owl]]
  [:require [tawny.reasoner :as r]]
  )

(defontology only-range
  :iri "http://onlyness"
  :prefix "only:"
  )

(defclass A)
(defclass B)

(defoproperty R
  :domain A
  :range B)



(defclass C
  :equivalent (only R B))

(r/reasoner-factory :hermit)

;; returns true -- C is equivalent to owl:thing. 
(r/iequivalent-class? C (owlthing))
(r/iequivalent-classes C)


;; In this case, C has become equivalent to owl:thing, which in turn means
;; that it is the superclass of everything. This is all rather unfortunate,
;; because if C has been declared as disjoint with anything -- very likely if
;; C is a subclass of anything else -- then the ontology will become
;; inconsistent. 

;; The flaw with the logic here is that R has a range of B. So we have already
;; restricted our world to only contain individuals where anything with an
;; outgoing R edge is of type B. Making Class C equivalent to any individual
;; where any outgoing R edge is of type B means that C is equivalent to any
;; individual, or owl:thing. 

;; It is possible to use universal ("only") qualifiers on a defined
;; (":equvialent") class however. Consider this example.

(defclass D)
(defclass E
  :equivalent (only R D))

(r/iequivalent-class? E (owlthing))
(r/iequivalent-classes E)

;; In this case, E is equivalent to (only R D). Now, this means that all R
;; edges from a E must end up in a D. But there could be other many Ds with no
;; incoming R edges. So, D is not forced to be a B. 



(save-ontology "only.omn" :omn)
(save-ontology "only.owl" :owl)
